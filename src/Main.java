import service.SistemaReserva;

import java.util.InputMismatchException;
import java.util.Scanner;

// Ponto de entrada do programa. Responsável pela interface com o usuário:
// exibe o menu, lê as entradas e delega as ações para SistemaReserva.
// Não contém lógica de negócio — apenas leitura de input e chamada de métodos.
public class Main {

    // Scanner compartilhado por toda a classe para ler entradas do teclado
    private static final Scanner scanner = new Scanner(System.in);

    // Instância única do sistema — carrega os dados do JSON ao ser criada
    private static final SistemaReserva sistema = new SistemaReserva();

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   QUADRA FACIL - Sistema de Reservas     ");
        System.out.println("===========================================");

        // Loop principal: repete o menu até o usuário digitar 0 (Sair)
        int opcao = -1;
        do {
            exibirMenu();
            opcao = lerInteiro("Opcao: ");
            processarOpcao(opcao);
        } while (opcao != 0);

        System.out.println("Sistema encerrado. Ate logo!");
        scanner.close(); // libera o recurso de leitura do teclado
    }

    // Imprime as opções disponíveis no terminal.
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

    // Direciona a opção escolhida para o método correto.
    // Captura erros de negócio (IllegalArgumentException, IllegalStateException)
    // e os exibe de forma amigável sem derrubar o programa.
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
                case 0 -> {} // sair — o loop verifica a condição e encerra
                default -> System.out.println("Opcao invalida. Tente novamente.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Erros esperados de validação ou regra de negócio — exibe a mensagem e continua
            System.out.println("ERRO: " + e.getMessage());
        } catch (Exception e) {
            // Erros inesperados — exibe e continua sem derrubar o programa
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    // ── Handlers de menu ─────────────────────────────────────────────────────

    // Lê os dados da quadra no terminal e repassa para o serviço.
    private static void cadastrarQuadra() {
        System.out.println("\n-- Cadastrar Quadra --");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Tipo (futebol, volei, basquete...): ");
        String tipo = scanner.nextLine().trim();
        double preco = lerDouble("Preco por hora (R$): ");
        sistema.cadastrarQuadra(nome, tipo, preco);
    }

    // Lê os dados do cliente no terminal e repassa para o serviço.
    private static void cadastrarCliente() {
        System.out.println("\n-- Cadastrar Cliente --");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine().trim();
        sistema.cadastrarCliente(nome, telefone);
    }

    // Exibe as listas de quadras e clientes para consulta, depois lê os IDs e o horário.
    private static void realizarReserva() {
        System.out.println("\n-- Realizar Reserva --");
        sistema.listarQuadras();  // exibe quadras para o usuário escolher o ID
        int quadraId = lerInteiro("ID da quadra: ");
        sistema.listarClientes(); // exibe clientes para o usuário escolher o ID
        int clienteId = lerInteiro("ID do cliente: ");
        System.out.print("Data (dd/MM/yyyy): ");
        String data = scanner.nextLine().trim();
        System.out.print("Hora (HH:mm): ");
        String hora = scanner.nextLine().trim();
        sistema.realizarReserva(clienteId, quadraId, data, hora);
    }

    // Exibe os clientes, lê o ID e delega a busca para o serviço.
    private static void buscarReservasPorCliente() {
        System.out.println("\n-- Buscar Reservas por Cliente --");
        sistema.listarClientes();
        int clienteId = lerInteiro("ID do cliente: ");
        sistema.buscarReservasPorCliente(clienteId);
    }

    // Exibe as quadras, lê o ID e o horário, e delega a verificação para o serviço.
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

    // Lê um número inteiro do teclado com segurança.
    // Se o usuário digitar texto ou deixar em branco, descarta a entrada e pede novamente.
    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                int valor = scanner.nextInt();
                scanner.nextLine(); // consome o Enter que sobrou no buffer após nextInt()
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // descarta a entrada inválida do buffer
                System.out.println("Entrada invalida. Informe um numero inteiro.");
            }
        }
    }

    // Lê um número decimal do teclado com segurança.
    // Se o usuário digitar texto ou valor inválido, descarta e pede novamente.
    private static double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                double valor = scanner.nextDouble();
                scanner.nextLine(); // consome o Enter que sobrou no buffer após nextDouble()
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // descarta a entrada inválida do buffer
                System.out.println("Entrada invalida. Informe um numero decimal.");
            }
        }
    }
}
