package model;

public class Quadra {
    private int id;
    private String nome;
    private String tipo;
    private double precoHora;

    public Quadra(int id, String nome, String tipo, double precoHora) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.precoHora = precoHora;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public double getPrecoHora() { return precoHora; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setPrecoHora(double precoHora) { this.precoHora = precoHora; }

    @Override
    public String toString() {
        return String.format("[ID: %d] %s | Tipo: %s | Preço/hora: R$ %.2f", id, nome, tipo, precoHora);
    }
}
