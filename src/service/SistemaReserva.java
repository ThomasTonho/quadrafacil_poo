package service;

import model.Cliente;
import model.Quadra;
import model.Reserva;
import util.ArquivoUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SistemaReserva {

    private List<Quadra> quadras;
    private List<Cliente> clientes;
    private List<Reserva> reservas;

    public SistemaReserva() {
        quadras = ArquivoUtil.lerQuadras();
        clientes = ArquivoUtil.lerClientes();
        reservas = ArquivoUtil.lerReservas();
    }

    // ── Quadras ──────────────────────────────────────────────────────────────

    public void cadastrarQuadra(String nome, String tipo, double precoHora) {
        if (nome == null || nome.trim().isEmpty() || tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e tipo são obrigatórios.");
        }
        if (precoHora <= 0) {
            throw new IllegalArgumentException("Preço por hora deve ser maior que zero.");
        }
        int novoId = gerarIdQuadra();
        Quadra q = new Quadra(novoId, nome.trim(), tipo.trim(), precoHora);
        quadras.add(q);
        ArquivoUtil.salvarQuadras(quadras);
        System.out.println("Quadra cadastrada com sucesso! " + q);
    }

    public void listarQuadras() {
        if (quadras.isEmpty()) {
            System.out.println("Nenhuma quadra cadastrada.");
            return;
        }
        System.out.println("\n=== QUADRAS CADASTRADAS ===");
        quadras.forEach(System.out::println);
    }

    // ── Clientes ─────────────────────────────────────────────────────────────

    public void cadastrarCliente(String nome, String telefone) {
        if (nome == null || nome.trim().isEmpty() || telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e telefone são obrigatórios.");
        }
        int novoId = gerarIdCliente();
        Cliente c = new Cliente(novoId, nome.trim(), telefone.trim());
        clientes.add(c);
        ArquivoUtil.salvarClientes(clientes);
        System.out.println("Cliente cadastrado com sucesso! " + c);
    }

    public void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.println("\n=== CLIENTES CADASTRADOS ===");
        clientes.forEach(System.out::println);
    }

    // ── Reservas ─────────────────────────────────────────────────────────────

    public void realizarReserva(int clienteId, int quadraId, String data, String hora) {
        if (data == null || data.trim().isEmpty() || hora == null || hora.trim().isEmpty()) {
            throw new IllegalArgumentException("Data e hora são obrigatórias.");
        }

        Cliente cliente = buscarClientePorId(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente com ID " + clienteId + " não encontrado.");
        }

        Quadra quadra = buscarQuadraPorId(quadraId);
        if (quadra == null) {
            throw new IllegalArgumentException("Quadra com ID " + quadraId + " não encontrada.");
        }

        if (!verificarDisponibilidade(quadraId, data.trim(), hora.trim())) {
            throw new IllegalStateException("Conflito de horário: a quadra já está reservada neste dia e horário.");
        }

        int novoId = gerarIdReserva();
        Reserva r = new Reserva(novoId, clienteId, quadraId, data.trim(), hora.trim());
        reservas.add(r);
        ArquivoUtil.salvarReservas(reservas);
        System.out.println("Reserva realizada com sucesso! " + r);
    }

    public void listarReservas() {
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva cadastrada.");
            return;
        }
        List<Reserva> ordenadas = new ArrayList<>(reservas);
        ordenadas.sort(Comparator.comparing(Reserva::getData).thenComparing(Reserva::getHora));
        System.out.println("\n=== RESERVAS (ordenadas por data) ===");
        for (Reserva r : ordenadas) {
            Cliente c = buscarClientePorId(r.getClienteId());
            Quadra q = buscarQuadraPorId(r.getQuadraId());
            String nomeCliente = (c != null) ? c.getNome() : "Desconhecido";
            String nomeQuadra = (q != null) ? q.getNome() : "Desconhecida";
            System.out.printf("[ID: %d] %s | Quadra: %s | Data: %s | Hora: %s%n",
                    r.getId(), nomeCliente, nomeQuadra, r.getData(), r.getHora());
        }
    }

    public void buscarReservasPorCliente(int clienteId) {
        Cliente cliente = buscarClientePorId(clienteId);
        if (cliente == null) {
            System.out.println("Cliente com ID " + clienteId + " não encontrado.");
            return;
        }
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getClienteId() == clienteId) {
                resultado.add(r);
            }
        }
        if (resultado.isEmpty()) {
            System.out.println("Nenhuma reserva encontrada para o cliente: " + cliente.getNome());
            return;
        }
        System.out.println("\n=== RESERVAS DE " + cliente.getNome().toUpperCase() + " ===");
        for (Reserva r : resultado) {
            Quadra q = buscarQuadraPorId(r.getQuadraId());
            String nomeQuadra = (q != null) ? q.getNome() : "Desconhecida";
            System.out.printf("[ID: %d] Quadra: %s | Data: %s | Hora: %s%n",
                    r.getId(), nomeQuadra, r.getData(), r.getHora());
        }
    }

    public boolean verificarDisponibilidade(int quadraId, String data, String hora) {
        for (Reserva r : reservas) {
            if (r.getQuadraId() == quadraId
                    && r.getData().equals(data)
                    && r.getHora().equals(hora)) {
                return false;
            }
        }
        return true;
    }

    public void verificarDisponibilidadeExibir(int quadraId, String data, String hora) {
        Quadra quadra = buscarQuadraPorId(quadraId);
        if (quadra == null) {
            System.out.println("Quadra com ID " + quadraId + " não encontrada.");
            return;
        }
        boolean disponivel = verificarDisponibilidade(quadraId, data, hora);
        if (disponivel) {
            System.out.printf("A quadra '%s' está DISPONÍVEL em %s às %s.%n", quadra.getNome(), data, hora);
        } else {
            System.out.printf("A quadra '%s' está OCUPADA em %s às %s.%n", quadra.getNome(), data, hora);
        }
    }

    // ── Helpers internos ─────────────────────────────────────────────────────

    private Cliente buscarClientePorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    private Quadra buscarQuadraPorId(int id) {
        for (Quadra q : quadras) {
            if (q.getId() == id) return q;
        }
        return null;
    }

    private int gerarIdQuadra() {
        return quadras.stream().mapToInt(Quadra::getId).max().orElse(0) + 1;
    }

    private int gerarIdCliente() {
        return clientes.stream().mapToInt(Cliente::getId).max().orElse(0) + 1;
    }

    private int gerarIdReserva() {
        return reservas.stream().mapToInt(Reserva::getId).max().orElse(0) + 1;
    }
}
