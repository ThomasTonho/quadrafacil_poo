package model;

public class Reserva {
    private int id;
    private int clienteId;
    private int quadraId;
    private String data;
    private String hora;

    public Reserva(int id, int clienteId, int quadraId, String data, String hora) {
        this.id = id;
        this.clienteId = clienteId;
        this.quadraId = quadraId;
        this.data = data;
        this.hora = hora;
    }

    public int getId() { return id; }
    public int getClienteId() { return clienteId; }
    public int getQuadraId() { return quadraId; }
    public String getData() { return data; }
    public String getHora() { return hora; }

    public void setId(int id) { this.id = id; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public void setQuadraId(int quadraId) { this.quadraId = quadraId; }
    public void setData(String data) { this.data = data; }
    public void setHora(String hora) { this.hora = hora; }

    @Override
    public String toString() {
        return String.format("[ID: %d] Cliente ID: %d | Quadra ID: %d | Data: %s | Hora: %s",
                id, clienteId, quadraId, data, hora);
    }
}
