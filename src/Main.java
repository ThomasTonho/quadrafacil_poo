import service.SistemaReserva;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final SistemaReserva sistema = new SistemaReserva();

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   QUADRA FACIL - Sistema de Reservas     ");
        System.out.println("===========================================");

        int opcao = -1;
        do {
            exibirMenu();
            opcao = lerInteiro("Opcao: ");
            processarOpcao(opcao);
        } while (opcao != 0);

        System.out.println("Sistema encerrado. Ate logo!");
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1 - Cadastrar Quadra");
        System.out.println("2 - Listar Quadras");
        System.out.println("3 - Cadastrar Cliente");
        System.out.println("4 - Listar Clientes");
        System.out.println("5 - Realizar Reserva");
        System.out.println("6 - Listar Reservas");
        System.out.println("7 - Buscar Reservas por Cliente");
        System.out.println("8 - Verificar Disponibilidade");
        System.out.println("9 - Listar Quadras por Tipo e Preco");
        System.out.println("0 - Sair");
    }

    private static void processarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 1 -> cadastrarQuadra();
                case 2 -> sistema.listarQuadras();
                case 3 -> cadastrarCliente();
                case 4 -> sistema.listarClientes();
                case 5 -> realizarReserva();
                case 6 -> sistema.listarReservas();
                case 7 -> buscarReservasPorCliente();
                case 8 -> verificarDisponibilidade();
                case 9 -> sistema.listarQuadrasOrdenadas();
                case 0 -> {} // sair
                default -> System.out.println("Opcao invalida. Tente novamente.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    // ── Handlers de menu ─────────────────────────────────────────────────────

    private static void cadastrarQuadra() {
        System.out.println("\n-- Cadastrar Quadra --");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Tipo (futebol, volei, basquete...): ");
        String tipo = scanner.nextLine().trim();
        double preco = lerDouble("Preco por hora (R$): ");
        sistema.cadastrarQuadra(nome, tipo, preco);
    }

    private static void cadastrarCliente() {
        System.out.println("\n-- Cadastrar Cliente --");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine().trim();
        sistema.cadastrarCliente(nome, telefone);
    }

    private static void realizarReserva() {
        System.out.println("\n-- Realizar Reserva --");
        sistema.listarQuadras();
        int quadraId = lerInteiro("ID da quadra: ");
        sistema.listarClientes();
        int clienteId = lerInteiro("ID do cliente: ");
        System.out.print("Data (dd/MM/yyyy): ");
        String data = scanner.nextLine().trim();
        System.out.print("Hora (HH:mm): ");
        String hora = scanner.nextLine().trim();
        sistema.realizarReserva(clienteId, quadraId, data, hora);
    }

    private static void buscarReservasPorCliente() {
        System.out.println("\n-- Buscar Reservas por Cliente --");
        sistema.listarClientes();
        int clienteId = lerInteiro("ID do cliente: ");
        sistema.buscarReservasPorCliente(clienteId);
    }

    private static void verificarDisponibilidade() {
        System.out.println("\n-- Verificar Disponibilidade --");
        sistema.listarQuadras();
        int quadraId = lerInteiro("ID da quadra: ");
        System.out.print("Data (dd/MM/yyyy): ");
        String data = scanner.nextLine().trim();
        System.out.print("Hora (HH:mm): ");
        String hora = scanner.nextLine().trim();
        sistema.verificarDisponibilidadeExibir(quadraId, data, hora);
    }

    // ── Utilitários de leitura ────────────────────────────────────────────────

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Entrada invalida. Informe um numero inteiro.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Entrada invalida. Informe um numero decimal.");
            }
        }
    }
}
