# EXPLICAÇÃO DO SISTEMA — Quadra Fácil

## O que é o sistema?

O Quadra Fácil é um sistema de gerenciamento de reservas de quadras esportivas desenvolvido em Java.
Ele permite cadastrar quadras, cadastrar clientes e realizar reservas, tudo pelo terminal.
Os dados são salvos em arquivos CSV para que não se percam ao fechar o programa.

---

## Por que Java e orientação a objetos?

O sistema usa o paradigma de **Programação Orientada a Objetos (POO)**, que organiza o código
em classes que representam entidades do mundo real. Isso deixa o código mais organizado,
fácil de entender e de expandir no futuro.

Os conceitos de POO aplicados foram:
- **Encapsulamento:** os atributos das classes são privados e só acessados via getters/setters
- **Abstração:** cada classe representa uma coisa real (Quadra, Cliente, Reserva)
- **Modularidade:** o código é dividido em camadas com responsabilidades separadas

---

## Estrutura do projeto (camadas)

O projeto é dividido em 4 camadas:

### model/ — Os dados
Contém as classes que representam as entidades do sistema.
Cada classe tem atributos, construtor, getters, setters e toString.

- **Quadra.java** — representa uma quadra esportiva (id, nome, tipo, preço por hora)
- **Cliente.java** — representa um cliente (id, nome, telefone)
- **Reserva.java** — representa uma reserva (id, clienteId, quadraId, data, hora)

### service/ — A lógica
Contém a classe `SistemaReserva.java`, que é o cérebro do sistema.
Ela gerencia as listas de quadras, clientes e reservas, e aplica todas as regras de negócio.

### util/ — Os arquivos
Contém a classe `ArquivoUtil.java`, responsável por ler e salvar os dados em arquivos CSV.
Ela é chamada pelo service sempre que algo é cadastrado ou o sistema inicia.

### Main.java — A interface
É o ponto de entrada do programa. Exibe o menu no terminal, lê a entrada do usuário
e chama os métodos do service conforme a opção escolhida.

---

## Como os dados são salvos?

Foi usado o formato **CSV (valores separados por vírgula)**, que é simples e funciona como
uma planilha em texto puro. Cada linha é um registro.

Exemplos:
```
quadras.csv  →  1,Arena Central,futebol,80.0
clientes.csv →  1,João Silva,11999998888
reservas.csv →  1,1,1,20/05/2025,14:00
```

Ao iniciar o programa, todos os arquivos são lidos e carregados na memória (ArrayLists).
A cada cadastro, o arquivo correspondente é sobrescrito com os dados atualizados.

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
- Se um arquivo CSV não existir ainda, o sistema começa com lista vazia (sem erro)
- Erros de negócio (cliente inexistente, conflito de horário) mostram mensagem clara ao usuário

---

## Estrutura de dados usada

Foi utilizado `ArrayList` para armazenar as listas de quadras, clientes e reservas.
O ArrayList foi escolhido por ser dinâmico (cresce conforme necessário) e de fácil iteração.

---

## Fluxo de execução resumido

```
Início
  → Carrega os arquivos CSV para as listas em memória
  → Exibe o menu
  → Usuário escolhe uma opção
  → Sistema executa a ação (valida, processa, salva)
  → Volta ao menu
  → Repete até o usuário digitar 0 (Sair)
```
