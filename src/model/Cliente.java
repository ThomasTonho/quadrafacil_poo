package model;

// Representa um cliente cadastrado no sistema.
// É um objeto imutável: todos os dados são definidos no construtor
// e acessados somente via getters, sem possibilidade de alteração posterior.
public class Cliente {

    // Atributos privados — só acessíveis de fora por meio dos getters (encapsulamento)
    private int id;         // identificador único gerado automaticamente pelo sistema
    private String nome;    // nome completo do cliente
    private String telefone; // contato do cliente

    // Construtor: recebe todos os dados de uma vez e preenche os atributos.
    public Cliente(int id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    // Getters: permitem leitura dos atributos sem expor o campo diretamente
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }

    // Retorna uma representação legível do cliente para exibição no terminal.
    @Override
    public String toString() {
        return String.format("[ID: %d] %s | Telefone: %s", id, nome, telefone);
    }
}
