package model;

// Representa uma reserva feita por um cliente para uma quadra em um horário específico.
// Não guarda os objetos Cliente e Quadra diretamente — apenas os IDs deles.
// Isso evita duplicação de dados: a busca pelo nome é feita na hora da exibição (em SistemaReserva).
public class Reserva {

    // Atributos privados — só acessíveis de fora por meio dos getters (encapsulamento)
    private int id;         // identificador único da reserva
    private int clienteId;  // referência ao cliente (equivale a uma chave estrangeira)
    private int quadraId;   // referência à quadra (equivale a uma chave estrangeira)
    private String data;    // data da reserva no formato dd/MM/yyyy
    private String hora;    // horário da reserva no formato HH:mm

    // Construtor: recebe todos os dados de uma vez e preenche os atributos.
    public Reserva(int id, int clienteId, int quadraId, String data, String hora) {
        this.id = id;
        this.clienteId = clienteId;
        this.quadraId = quadraId;
        this.data = data;
        this.hora = hora;
    }

    // Getters: permitem leitura dos atributos sem expor o campo diretamente
    public int getId() { return id; }
    public int getClienteId() { return clienteId; }
    public int getQuadraId() { return quadraId; }
    public String getData() { return data; }
    public String getHora() { return hora; }

    // Retorna uma representação legível da reserva para exibição no terminal.
    @Override
    public String toString() {
        return String.format("[ID: %d] Cliente ID: %d | Quadra ID: %d | Data: %s | Hora: %s",
                id, clienteId, quadraId, data, hora);
    }
}
