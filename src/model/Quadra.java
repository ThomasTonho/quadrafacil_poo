package model;

// Representa uma quadra esportiva cadastrada no sistema.
// É um objeto imutável: todos os dados são definidos no construtor
// e acessados somente via getters, sem possibilidade de alteração posterior.
public class Quadra {

    // Atributos privados — só acessíveis de fora por meio dos getters (encapsulamento)
    private int id;          // identificador único gerado automaticamente pelo sistema
    private String nome;     // nome da quadra (ex: "Arena Central")
    private String tipo;     // modalidade (ex: "futebol", "volei", "basquete")
    private double precoHora; // valor cobrado por hora de uso

    // Construtor: recebe todos os dados de uma vez e preenche os atributos.
    // O prefixo "this." distingue o atributo da classe do parâmetro de mesmo nome.
    public Quadra(int id, String nome, String tipo, double precoHora) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.precoHora = precoHora;
    }

    // Getters: permitem leitura dos atributos sem expor o campo diretamente
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public double getPrecoHora() { return precoHora; }

    // Retorna uma representação legível da quadra para exibição no terminal.
    // Chamado automaticamente pelo System.out.println(quadra).
    @Override
    public String toString() {
        return String.format("[ID: %d] %s | Tipo: %s | Preço/hora: R$ %.2f", id, nome, tipo, precoHora);
    }
}
