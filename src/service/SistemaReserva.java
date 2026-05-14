package service;

import model.Cliente;
import model.Quadra;
import model.Reserva;
import util.ArquivoUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Camada de serviço: contém toda a lógica de negócio do sistema.
// Mantém as três listas em memória (quadras, clientes, reservas) e
// coordena a validação, o processamento e a persistência dos dados.
public class SistemaReserva {

    // Listas em memória — carregadas do JSON ao iniciar e atualizadas a cada operação
    private List<Quadra> quadras;
    private List<Cliente> clientes;
    private List<Reserva> reservas;

    // Construtor: ao criar o sistema, carrega imediatamente os dados salvos nos arquivos JSON.
    // Se os arquivos não existirem ainda, ArquivoUtil retorna listas vazias (sem erro).
    public SistemaReserva() {
        quadras = ArquivoUtil.lerQuadras();
        clientes = ArquivoUtil.lerClientes();
        reservas = ArquivoUtil.lerReservas();
    }

    // ── Quadras ──────────────────────────────────────────────────────────────

    // Valida os dados, cria o objeto Quadra com ID automático, adiciona na lista
    // e persiste toda a lista atualizada no arquivo JSON.
    public void cadastrarQuadra(String nome, String tipo, double precoHora) {
        // Validação: campos obrigatórios não podem ser nulos ou vazios
        if (nome == null || nome.trim().isEmpty() || tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e tipo são obrigatórios.");
        }
        // Validação: preço deve ser positivo
        if (precoHora <= 0) {
            throw new IllegalArgumentException("Preço por hora deve ser maior que zero.");
        }
        int novoId = gerarIdQuadra(); // ID = maior ID existente + 1
        Quadra q = new Quadra(novoId, nome.trim(), tipo.trim(), precoHora);
        quadras.add(q);
        ArquivoUtil.salvarQuadras(quadras); // persiste a lista completa no JSON
        System.out.println("Quadra cadastrada com sucesso! " + q);
    }

    // Percorre a lista em memória e imprime cada quadra usando o toString() da classe.
    public void listarQuadras() {
        if (quadras.isEmpty()) {
            System.out.println("Nenhuma quadra cadastrada.");
            return;
        }
        System.out.println("\n=== QUADRAS CADASTRADAS ===");
        quadras.forEach(System.out::println); // equivale a: for (Quadra q : quadras) System.out.println(q)
    }

    // Cria uma cópia da lista para não alterar a ordem original,
    // ordena primeiro por tipo (alfabético) e depois pelo menor preço.
    public void listarQuadrasOrdenadas() {
        if (quadras.isEmpty()) {
            System.out.println("Nenhuma quadra cadastrada.");
            return;
        }
        List<Quadra> ordenadas = new ArrayList<>(quadras); // cópia — a lista original não é modificada
        ordenadas.sort(Comparator.comparing(Quadra::getTipo).thenComparingDouble(Quadra::getPrecoHora));
        System.out.println("\n=== QUADRAS ORDENADAS POR TIPO E PRECO ===");
        ordenadas.forEach(System.out::println);
    }

    // ── Clientes ─────────────────────────────────────────────────────────────

    // Valida os dados, cria o objeto Cliente com ID automático, adiciona na lista
    // e persiste toda a lista atualizada no arquivo JSON.
    public void cadastrarCliente(String nome, String telefone) {
        if (nome == null || nome.trim().isEmpty() || telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e telefone são obrigatórios.");
        }
        int novoId = gerarIdCliente();
        Cliente c = new Cliente(novoId, nome.trim(), telefone.trim());
        clientes.add(c);
        ArquivoUtil.salvarClientes(clientes); // persiste a lista completa no JSON
        System.out.println("Cliente cadastrado com sucesso! " + c);
    }

    // Percorre a lista em memória e imprime cada cliente.
    public void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.println("\n=== CLIENTES CADASTRADOS ===");
        clientes.forEach(System.out::println);
    }

    // ── Reservas ─────────────────────────────────────────────────────────────

    // Valida dados, verifica se cliente e quadra existem, verifica conflito de horário,
    // cria a reserva com ID automático e persiste no JSON.
    public void realizarReserva(int clienteId, int quadraId, String data, String hora) {
        if (data == null || data.trim().isEmpty() || hora == null || hora.trim().isEmpty()) {
            throw new IllegalArgumentException("Data e hora são obrigatórias.");
        }

        // Busca o cliente pelo ID — retorna null se não encontrado
        Cliente cliente = buscarClientePorId(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente com ID " + clienteId + " não encontrado.");
        }

        // Busca a quadra pelo ID — retorna null se não encontrada
        Quadra quadra = buscarQuadraPorId(quadraId);
        if (quadra == null) {
            throw new IllegalArgumentException("Quadra com ID " + quadraId + " não encontrada.");
        }

        // Verifica se já existe uma reserva para essa quadra na mesma data e hora
        if (!verificarDisponibilidade(quadraId, data.trim(), hora.trim())) {
            throw new IllegalStateException("Conflito de horário: a quadra já está reservada neste dia e horário.");
        }

        int novoId = gerarIdReserva();
        Reserva r = new Reserva(novoId, clienteId, quadraId, data.trim(), hora.trim());
        reservas.add(r);
        ArquivoUtil.salvarReservas(reservas); // persiste a lista completa no JSON
        System.out.println("Reserva realizada com sucesso! " + r);
    }

    // Lista todas as reservas ordenadas por data e hora.
    // Para cada reserva, resolve os IDs para os nomes reais de cliente e quadra.
    public void listarReservas() {
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva cadastrada.");
            return;
        }
        List<Reserva> ordenadas = new ArrayList<>(reservas); // cópia para não alterar a ordem original
        ordenadas.sort(Comparator.comparing(Reserva::getData).thenComparing(Reserva::getHora));
        System.out.println("\n=== RESERVAS (ordenadas por data) ===");
        for (Reserva r : ordenadas) {
            // Busca os nomes a partir dos IDs armazenados na reserva
            Cliente c = buscarClientePorId(r.getClienteId());
            Quadra q = buscarQuadraPorId(r.getQuadraId());
            String nomeCliente = (c != null) ? c.getNome() : "Desconhecido";
            String nomeQuadra = (q != null) ? q.getNome() : "Desconhecida";
            System.out.printf("[ID: %d] %s | Quadra: %s | Data: %s | Hora: %s%n",
                    r.getId(), nomeCliente, nomeQuadra, r.getData(), r.getHora());
        }
    }

    // Filtra e exibe somente as reservas do cliente informado.
    public void buscarReservasPorCliente(int clienteId) {
        Cliente cliente = buscarClientePorId(clienteId);
        if (cliente == null) {
            System.out.println("Cliente com ID " + clienteId + " não encontrado.");
            return;
        }
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getClienteId() == clienteId) { // filtra pelo ID do cliente
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

    // Verifica se existe alguma reserva com a mesma combinação de quadra + data + hora.
    // Retorna true se estiver livre, false se estiver ocupada.
    // Usado internamente por realizarReserva e externamente por verificarDisponibilidadeExibir.
    public boolean verificarDisponibilidade(int quadraId, String data, String hora) {
        for (Reserva r : reservas) {
            if (r.getQuadraId() == quadraId
                    && r.getData().equals(data)
                    && r.getHora().equals(hora)) {
                return false; // conflito encontrado
            }
        }
        return true; // nenhuma reserva conflitante
    }

    // Versão de verificarDisponibilidade para exibição no terminal (opção 8 do menu).
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

    // Percorre a lista procurando o cliente com o ID informado. Retorna null se não achar.
    private Cliente buscarClientePorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    // Percorre a lista procurando a quadra com o ID informado. Retorna null se não achar.
    private Quadra buscarQuadraPorId(int id) {
        for (Quadra q : quadras) {
            if (q.getId() == id) return q;
        }
        return null;
    }

    // Gera um novo ID para quadra pegando o maior ID existente e somando 1.
    // Se a lista estiver vazia, começa do 1.
    private int gerarIdQuadra() {
        return quadras.stream().mapToInt(Quadra::getId).max().orElse(0) + 1;
    }

    // Mesma lógica de gerarIdQuadra, aplicada para clientes.
    private int gerarIdCliente() {
        return clientes.stream().mapToInt(Cliente::getId).max().orElse(0) + 1;
    }

    // Mesma lógica de gerarIdQuadra, aplicada para reservas.
    private int gerarIdReserva() {
        return reservas.stream().mapToInt(Reserva::getId).max().orElse(0) + 1;
    }
}
