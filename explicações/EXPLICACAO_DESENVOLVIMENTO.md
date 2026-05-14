# EXPLICAÇÃO DO SISTEMA — Quadra Fácil

## O que é o sistema?

O Quadra Fácil é um sistema de gerenciamento de reservas de quadras esportivas desenvolvido em Java.
Ele permite cadastrar quadras, cadastrar clientes e realizar reservas, tudo pelo terminal.
Os dados são salvos em arquivos JSON para que não se percam ao fechar o programa.

---

## Por que Java e orientação a objetos?

O sistema usa o paradigma de **Programação Orientada a Objetos (POO)**, que organiza o código
em classes que representam entidades do mundo real. Isso deixa o código mais organizado,
fácil de entender e de expandir no futuro.

Os conceitos de POO aplicados foram:
- **Encapsulamento:** os atributos das classes são privados e acessados somente via getters; os objetos são imutáveis após a criação
- **Abstração:** cada classe representa uma coisa real (Quadra, Cliente, Reserva)
- **Modularidade:** o código é dividido em camadas com responsabilidades separadas

---

## Estrutura do projeto (camadas)

O projeto é dividido em 4 camadas:

### model/ — Os dados
Contém as classes que representam as entidades do sistema.
Cada classe tem atributos, construtor, getters e toString.

- **Quadra.java** — representa uma quadra esportiva (id, nome, tipo, preço por hora)
- **Cliente.java** — representa um cliente (id, nome, telefone)
- **Reserva.java** — representa uma reserva (id, clienteId, quadraId, data, hora)

### service/ — A lógica
Contém a classe `SistemaReserva.java`, que é o cérebro do sistema.
Ela gerencia as listas de quadras, clientes e reservas, e aplica todas as regras de negócio.

### util/ — Os arquivos
Contém a classe `ArquivoUtil.java`, responsável por ler e salvar os dados em arquivos JSON.
Ela é chamada pelo service sempre que algo é cadastrado ou o sistema inicia.

### Main.java — A interface
É o ponto de entrada do programa. Exibe o menu no terminal, lê a entrada do usuário
e chama os métodos do service conforme a opção escolhida.

---

## Como os dados são salvos?

Foi usado o formato **JSON**, que é leve, legível e amplamente adotado.
Cada arquivo contém um array de objetos.

Exemplos:
```json
quadras.json  →  [{"id":1,"nome":"Arena Central","tipo":"futebol","precoHora":80.00}]
clientes.json →  [{"id":1,"nome":"João Silva","telefone":"11999998888"}]
reservas.json →  [{"id":1,"clienteId":1,"quadraId":1,"data":"20/05/2025","hora":"14:00"}]
```

Ao iniciar o programa, todos os arquivos são lidos e carregados na memória (ArrayLists).
A cada cadastro, o arquivo correspondente é sobrescrito com os dados atualizados.

---

## Manipulação de dados — como funciona e onde está no código

Esta é a parte central do sistema: como os dados saem do disco, são processados em memória
e voltam para o disco. O fluxo tem três etapas bem definidas.

---

### Etapa 1 — Leitura (disco → memória)

**Onde:** `ArquivoUtil.java` (métodos `lerQuadras`, `lerClientes`, `lerReservas`)
**Quando é chamado:** no construtor de `SistemaReserva.java` (linha `quadras = ArquivoUtil.lerQuadras()`)

O processo de leitura segue estes passos:

**1. Ler o arquivo inteiro como texto** — método `lerArquivo(caminho)`
```java
// Lê linha por linha e concatena tudo numa única String
StringBuilder sb = new StringBuilder();
while ((linha = br.readLine()) != null) {
    sb.append(linha);
}
```
O resultado é algo como: `[{"id":1,"nome":"Arena Central","tipo":"futebol","precoHora":80.00}]`

**2. Separar os objetos** — método `extrairObjetos(json)`
```java
// Encontra cada { } e extrai o trecho como String separada
int inicio = json.indexOf('{');
int fim = json.indexOf('}', inicio);
objetos.add(json.substring(inicio, fim + 1));
```
Resultado: uma lista de strings, cada uma sendo um objeto JSON individual.

**3. Extrair os valores de cada campo**

Para campos de texto — método `extrairString(obj, chave)`:
```java
// Busca o padrão "nome":"  e lê até a próxima aspas não escapada
String padrao = "\"nome\":\"";
// ... avança até encontrar " que não seja precedido por \
```

Para campos numéricos — método `extrairNumero(obj, chave)`:
```java
// Busca o padrão "id":  e lê os dígitos/ponto que vierem a seguir
String padrao = "\"id\":";
// ... avança enquanto o char for dígito, ponto ou sinal negativo
```

**4. Criar os objetos** com os valores extraídos:
```java
lista.add(new Quadra(id, nome, tipo, preco));
```

---

### Etapa 2 — Processamento em memória

**Onde:** `SistemaReserva.java` — atributos `quadras`, `clientes`, `reservas`

Após a leitura, os dados ficam em três `ArrayList`s:
```java
private List<Quadra> quadras;
private List<Cliente> clientes;
private List<Reserva> reservas;
```

Todas as operações do sistema trabalham sobre essas listas:

| Operação | Método em SistemaReserva | O que faz na lista |
|---|---|---|
| Cadastrar | `cadastrarQuadra`, `cadastrarCliente`, `realizarReserva` | `.add(objeto)` |
| Listar | `listarQuadras`, `listarClientes`, `listarReservas` | percorre com `for` ou `forEach` |
| Buscar | `buscarClientePorId`, `buscarQuadraPorId` | percorre com `for` até achar o ID |
| Filtrar | `buscarReservasPorCliente` | percorre e coleta os que batem com o critério |
| Ordenar | `listarQuadrasOrdenadas`, `listarReservas` | cria cópia e usa `sort` com `Comparator` |
| Verificar | `verificarDisponibilidade` | percorre e compara quadraId + data + hora |

**IDs automáticos:** ao cadastrar qualquer entidade, o sistema gera um novo ID assim:
```java
// Pega o maior ID existente na lista e soma 1. Se a lista estiver vazia, começa do 1.
return quadras.stream().mapToInt(Quadra::getId).max().orElse(0) + 1;
```

**Resolução de referências:** a `Reserva` guarda apenas os IDs do cliente e da quadra,
não os objetos completos. Na hora de exibir, o service resolve os nomes:
```java
// Em listarReservas():
Cliente c = buscarClientePorId(r.getClienteId());
Quadra q = buscarQuadraPorId(r.getQuadraId());
String nomeCliente = (c != null) ? c.getNome() : "Desconhecido";
```

---

### Etapa 3 — Persistência (memória → disco)

**Onde:** `ArquivoUtil.java` (métodos `salvarQuadras`, `salvarClientes`, `salvarReservas`)
**Quando é chamado:** imediatamente após cada `.add()` bem-sucedido, em `SistemaReserva.java`

```java
// Exemplo em cadastrarQuadra():
quadras.add(q);
ArquivoUtil.salvarQuadras(quadras); // persiste a lista inteira
```

O processo de escrita serializa a lista inteira em formato JSON:
```java
pw.println("[");                          // abre o array
for (int i = 0; i < quadras.size(); i++) {
    Quadra q = quadras.get(i);
    String virgula = (i < quadras.size() - 1) ? "," : ""; // vírgula exceto no último
    pw.printf(Locale.US,
        "  {\"id\":%d,\"nome\":\"%s\",\"tipo\":\"%s\",\"precoHora\":%.2f}%s%n",
        q.getId(), escapar(q.getNome()), escapar(q.getTipo()), q.getPrecoHora(), virgula);
}
pw.println("]");                          // fecha o array
```

O `Locale.US` garante que o número decimal use ponto (ex: `80.00`) e não vírgula,
já que a vírgula quebraria a estrutura do JSON.

O método `escapar` protege contra nomes que contenham aspas ou barras:
```java
// Ex: nome com aspas →  Arena "Sul"  vira  Arena \"Sul\"  no JSON
valor.replace("\\", "\\\\").replace("\"", "\\\"");
```

---

### Fluxo completo — exemplo de um cadastro

```
Usuário digita os dados no terminal (Main.java)
  → Main chama sistema.cadastrarQuadra(nome, tipo, preco)
    → SistemaReserva valida os dados
    → SistemaReserva gera o ID automático
    → SistemaReserva cria new Quadra(id, nome, tipo, preco)
    → SistemaReserva faz quadras.add(q)        ← lista em memória atualizada
    → SistemaReserva chama ArquivoUtil.salvarQuadras(quadras)
      → ArquivoUtil abre/cria quadras.json
      → ArquivoUtil serializa toda a lista para JSON
      → ArquivoUtil fecha o arquivo             ← disco atualizado
  → Sistema exibe confirmação no terminal
```

---

## Regras de negócio implementadas

### Regra principal — conflito de horário
Antes de salvar uma reserva, o sistema verifica se já existe outra reserva
com a mesma quadra + mesma data + mesma hora. Se existir, a reserva é bloqueada.

### Outras validações
- O cliente precisa estar cadastrado para fazer uma reserva
- A quadra precisa estar cadastrada para fazer uma reserva
- Campos obrigatórios não podem ser vazios
- IDs são gerados automaticamente (pega o maior ID existente + 1)

---

## Tratamento de erros

O sistema usa `try/catch` para evitar que o programa quebre:
- Se o usuário digitar uma letra onde se espera número, o sistema pede novamente
- Se um arquivo JSON não existir ainda, o sistema começa com lista vazia (sem erro)
- Erros de negócio (cliente inexistente, conflito de horário) mostram mensagem clara ao usuário

---

## Estrutura de dados usada

Foi utilizado `ArrayList` para armazenar as listas de quadras, clientes e reservas.
O ArrayList foi escolhido por ser dinâmico (cresce conforme necessário) e de fácil iteração.

---

## Fluxo de execução resumido

```
Início
  → Carrega os arquivos JSON para as listas em memória
  → Exibe o menu
  → Usuário escolhe uma opção
  → Sistema executa a ação (valida, processa, salva)
  → Volta ao menu
  → Repete até o usuário digitar 0 (Sair)
```
