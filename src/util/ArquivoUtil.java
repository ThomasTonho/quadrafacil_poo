package util;

import model.Cliente;
import model.Quadra;
import model.Reserva;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {

    private static final String DIR = "data/";
    private static final String ARQUIVO_QUADRAS = DIR + "quadras.csv";
    private static final String ARQUIVO_CLIENTES = DIR + "clientes.csv";
    private static final String ARQUIVO_RESERVAS = DIR + "reservas.csv";

    public static List<Quadra> lerQuadras() {
        List<Quadra> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_QUADRAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                String[] partes = linha.split(",");
                int id = Integer.parseInt(partes[0]);
                String nome = partes[1];
                String tipo = partes[2];
                double preco = Double.parseDouble(partes[3]);
                lista.add(new Quadra(id, nome, tipo, preco));
            }
        } catch (FileNotFoundException e) {
            // arquivo ainda não existe, retorna lista vazia
        } catch (Exception e) {
            System.out.println("Erro ao ler quadras: " + e.getMessage());
        }
        return lista;
    }

    public static void salvarQuadras(List<Quadra> quadras) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_QUADRAS))) {
            for (Quadra q : quadras) {
                pw.println(q.getId() + "," + q.getNome() + "," + q.getTipo() + "," + q.getPrecoHora());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar quadras: " + e.getMessage());
        }
    }

    public static List<Cliente> lerClientes() {
        List<Cliente> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_CLIENTES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                String[] partes = linha.split(",");
                int id = Integer.parseInt(partes[0]);
                String nome = partes[1];
                String telefone = partes[2];
                lista.add(new Cliente(id, nome, telefone));
            }
        } catch (FileNotFoundException e) {
            // arquivo ainda não existe, retorna lista vazia
        } catch (Exception e) {
            System.out.println("Erro ao ler clientes: " + e.getMessage());
        }
        return lista;
    }

    public static void salvarClientes(List<Cliente> clientes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_CLIENTES))) {
            for (Cliente c : clientes) {
                pw.println(c.getId() + "," + c.getNome() + "," + c.getTelefone());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    public static List<Reserva> lerReservas() {
        List<Reserva> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_RESERVAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                String[] partes = linha.split(",");
                int id = Integer.parseInt(partes[0]);
                int clienteId = Integer.parseInt(partes[1]);
                int quadraId = Integer.parseInt(partes[2]);
                String data = partes[3];
                String hora = partes[4];
                lista.add(new Reserva(id, clienteId, quadraId, data, hora));
            }
        } catch (FileNotFoundException e) {
            // arquivo ainda não existe, retorna lista vazia
        } catch (Exception e) {
            System.out.println("Erro ao ler reservas: " + e.getMessage());
        }
        return lista;
    }

    public static void salvarReservas(List<Reserva> reservas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_RESERVAS))) {
            for (Reserva r : reservas) {
                pw.println(r.getId() + "," + r.getClienteId() + "," + r.getQuadraId() + "," + r.getData() + "," + r.getHora());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar reservas: " + e.getMessage());
        }
    }
}
