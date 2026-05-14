package util;

import model.Cliente;
import model.Quadra;
import model.Reserva;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Camada utilitária responsável por toda a leitura e escrita de dados em disco.
// Não contém lógica de negócio — só sabe como transformar objetos em JSON e vice-versa.
// Não usa bibliotecas externas: o parser JSON foi implementado manualmente com operações de String.
public class ArquivoUtil {

    // Caminhos dos arquivos de dados (relativos à pasta onde o programa é executado)
    private static final String DIR = "data/";
    private static final String ARQUIVO_QUADRAS = DIR + "quadras.json";
    private static final String ARQUIVO_CLIENTES = DIR + "clientes.json";
    private static final String ARQUIVO_RESERVAS = DIR + "reservas.json";

    // ── Quadras ──────────────────────────────────────────────────────────────

    // Lê o arquivo quadras.json e reconstrói a lista de objetos Quadra.
    // Se o arquivo não existir ainda (primeiro uso), retorna lista vazia sem erro.
    public static List<Quadra> lerQuadras() {
        List<Quadra> lista = new ArrayList<>();
        try {
            String conteudo = lerArquivo(ARQUIVO_QUADRAS); // lê o arquivo inteiro como String
            for (String obj : extrairObjetos(conteudo)) {  // divide em objetos JSON individuais
                // Extrai cada campo pelo nome da chave e converte para o tipo correto
                int id = (int) extrairNumero(obj, "id");
                String nome = extrairString(obj, "nome");
                String tipo = extrairString(obj, "tipo");
                double preco = extrairNumero(obj, "precoHora");
                lista.add(new Quadra(id, nome, tipo, preco));
            }
        } catch (FileNotFoundException e) {
            // arquivo ainda não existe, retorna lista vazia
        } catch (Exception e) {
            System.out.println("Erro ao ler quadras: " + e.getMessage());
        }
        return lista;
    }

    // Serializa a lista de quadras para JSON e sobrescreve o arquivo.
    // A lista inteira é sempre rescrita — não há append, só substituição completa.
    public static void salvarQuadras(List<Quadra> quadras) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_QUADRAS))) {
            pw.println("["); // abre o array JSON
            for (int i = 0; i < quadras.size(); i++) {
                Quadra q = quadras.get(i);
                // vírgula após cada objeto, exceto o último (exigência do formato JSON)
                String virgula = (i < quadras.size() - 1) ? "," : "";
                // Locale.US garante ponto como separador decimal (padrão JSON)
                pw.printf(Locale.US, "  {\"id\":%d,\"nome\":\"%s\",\"tipo\":\"%s\",\"precoHora\":%.2f}%s%n",
                        q.getId(), escapar(q.getNome()), escapar(q.getTipo()), q.getPrecoHora(), virgula);
            }
            pw.println("]"); // fecha o array JSON
        } catch (IOException e) {
            System.out.println("Erro ao salvar quadras: " + e.getMessage());
        }
    }

    // ── Clientes ─────────────────────────────────────────────────────────────

    // Lê o arquivo clientes.json e reconstrói a lista de objetos Cliente.
    public static List<Cliente> lerClientes() {
        List<Cliente> lista = new ArrayList<>();
        try {
            String conteudo = lerArquivo(ARQUIVO_CLIENTES);
            for (String obj : extrairObjetos(conteudo)) {
                int id = (int) extrairNumero(obj, "id");
                String nome = extrairString(obj, "nome");
                String telefone = extrairString(obj, "telefone");
                lista.add(new Cliente(id, nome, telefone));
            }
        } catch (FileNotFoundException e) {
            // arquivo ainda não existe, retorna lista vazia
        } catch (Exception e) {
            System.out.println("Erro ao ler clientes: " + e.getMessage());
        }
        return lista;
    }

    // Serializa a lista de clientes para JSON e sobrescreve o arquivo.
    public static void salvarClientes(List<Cliente> clientes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_CLIENTES))) {
            pw.println("[");
            for (int i = 0; i < clientes.size(); i++) {
                Cliente c = clientes.get(i);
                String virgula = (i < clientes.size() - 1) ? "," : "";
                pw.printf("  {\"id\":%d,\"nome\":\"%s\",\"telefone\":\"%s\"}%s%n",
                        c.getId(), escapar(c.getNome()), escapar(c.getTelefone()), virgula);
            }
            pw.println("]");
        } catch (IOException e) {
            System.out.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    // ── Reservas ─────────────────────────────────────────────────────────────

    // Lê o arquivo reservas.json e reconstrói a lista de objetos Reserva.
    public static List<Reserva> lerReservas() {
        List<Reserva> lista = new ArrayList<>();
        try {
            String conteudo = lerArquivo(ARQUIVO_RESERVAS);
            for (String obj : extrairObjetos(conteudo)) {
                int id = (int) extrairNumero(obj, "id");
                int clienteId = (int) extrairNumero(obj, "clienteId");
                int quadraId = (int) extrairNumero(obj, "quadraId");
                String data = extrairString(obj, "data");
                String hora = extrairString(obj, "hora");
                lista.add(new Reserva(id, clienteId, quadraId, data, hora));
            }
        } catch (FileNotFoundException e) {
            // arquivo ainda não existe, retorna lista vazia
        } catch (Exception e) {
            System.out.println("Erro ao ler reservas: " + e.getMessage());
        }
        return lista;
    }

    // Serializa a lista de reservas para JSON e sobrescreve o arquivo.
    public static void salvarReservas(List<Reserva> reservas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_RESERVAS))) {
            pw.println("[");
            for (int i = 0; i < reservas.size(); i++) {
                Reserva r = reservas.get(i);
                String virgula = (i < reservas.size() - 1) ? "," : "";
                pw.printf("  {\"id\":%d,\"clienteId\":%d,\"quadraId\":%d,\"data\":\"%s\",\"hora\":\"%s\"}%s%n",
                        r.getId(), r.getClienteId(), r.getQuadraId(), r.getData(), r.getHora(), virgula);
            }
            pw.println("]");
        } catch (IOException e) {
            System.out.println("Erro ao salvar reservas: " + e.getMessage());
        }
    }

    // ── Helpers de JSON ───────────────────────────────────────────────────────

    // Lê o arquivo inteiro e retorna o conteúdo como uma única String.
    // Lança FileNotFoundException se o arquivo não existir (tratado pelos chamadores).
    private static String lerArquivo(String caminho) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha); // concatena todas as linhas sem quebras
            }
        }
        return sb.toString();
    }

    // Percorre o JSON e extrai cada objeto entre { } como uma String separada.
    // Exemplo: "[{...},{...}]" → lista com duas strings "{...}" e "{...}"
    // Funciona porque os objetos deste sistema não possuem objetos aninhados.
    private static List<String> extrairObjetos(String json) {
        List<String> objetos = new ArrayList<>();
        int inicio = json.indexOf('{');
        while (inicio != -1) {
            int fim = json.indexOf('}', inicio); // encontra o fechamento do objeto
            if (fim == -1) break;
            objetos.add(json.substring(inicio, fim + 1));
            inicio = json.indexOf('{', fim + 1); // avança para o próximo objeto
        }
        return objetos;
    }

    // Extrai o valor de um campo do tipo String de um objeto JSON.
    // Busca pelo padrão "chave":"valor" e devolve apenas o valor.
    // Trata aspas e barras escapadas (\", \\) de volta para os caracteres reais.
    private static String extrairString(String obj, String chave) {
        String padrao = "\"" + chave + "\":\""; // ex: "nome":"
        int inicio = obj.indexOf(padrao);
        if (inicio == -1) return ""; // chave não encontrada
        inicio += padrao.length(); // avança para o primeiro char do valor
        int fim = inicio;
        // avança até encontrar uma aspas que NÃO seja precedida por barra (ou seja, não está escapada)
        while (fim < obj.length()) {
            if (obj.charAt(fim) == '"' && obj.charAt(fim - 1) != '\\') break;
            fim++;
        }
        // desfaz o escape dos caracteres especiais (\" → " e \\ → \)
        return obj.substring(inicio, fim).replace("\\\"", "\"").replace("\\\\", "\\");
    }

    // Extrai o valor de um campo numérico (inteiro ou decimal) de um objeto JSON.
    // Busca pelo padrão "chave":numero e devolve o número como double.
    private static double extrairNumero(String obj, String chave) {
        String padrao = "\"" + chave + "\":"; // ex: "id":
        int inicio = obj.indexOf(padrao);
        if (inicio == -1) return 0;
        inicio += padrao.length(); // avança para o primeiro dígito
        int fim = inicio;
        // avança enquanto o caractere for parte de um número (dígito, ponto decimal ou sinal negativo)
        while (fim < obj.length() && (Character.isDigit(obj.charAt(fim)) || obj.charAt(fim) == '.' || obj.charAt(fim) == '-')) {
            fim++;
        }
        return Double.parseDouble(obj.substring(inicio, fim));
    }

    // Escapa caracteres especiais de uma String para que ela seja válida dentro de um JSON.
    // Barras invertidas e aspas precisam ser escapadas para não quebrar a estrutura do arquivo.
    private static String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
