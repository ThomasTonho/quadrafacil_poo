# GUIA DE USO — Quadra Fácil

## Como compilar e rodar

1. Abra o terminal na pasta do projeto
2. Compile:
   ```
   javac -d out -sourcepath src src/Main.java src/model/*.java src/util/*.java src/service/*.java
   ```
3. Rode:
   ```
   cd out
   java Main
   ```

---

## Menu principal

Ao iniciar, aparece esse menu:

```
1 - Cadastrar Quadra
2 - Listar Quadras
3 - Cadastrar Cliente
4 - Listar Clientes
5 - Realizar Reserva
6 - Listar Reservas
7 - Buscar Reservas por Cliente
8 - Verificar Disponibilidade
0 - Sair
```

Digite o número e pressione Enter.

---

## Passo a passo para fazer uma reserva

### 1. Cadastrar uma quadra (opção 1)
- Digite o nome da quadra (ex: Arena Central)
- Digite o tipo (ex: futebol, volei, basquete)
- Digite o preço por hora (ex: 80.0)

### 2. Cadastrar um cliente (opção 3)
- Digite o nome do cliente
- Digite o telefone

### 3. Realizar a reserva (opção 5)
- O sistema mostra as quadras disponíveis — anote o ID
- O sistema mostra os clientes — anote o ID
- Digite o ID da quadra
- Digite o ID do cliente
- Digite a data no formato dd/MM/yyyy (ex: 20/05/2025)
- Digite a hora no formato HH:mm (ex: 14:00)

Se o horário já estiver ocupado, o sistema bloqueia e avisa.

---

## Outras opções

**Listar Quadras (2):** mostra todas as quadras cadastradas com ID, nome, tipo e preço.

**Listar Clientes (4):** mostra todos os clientes com ID, nome e telefone.

**Listar Reservas (6):** mostra todas as reservas ordenadas por data, com nome do cliente e da quadra.

**Buscar Reservas por Cliente (7):** digite o ID do cliente e o sistema mostra só as reservas dele.

**Verificar Disponibilidade (8):** informe ID da quadra, data e hora — o sistema diz se está livre ou ocupada.

---

## Arquivos CSV (pasta data/)

Os dados ficam salvos automaticamente em:

| Arquivo | O que guarda |
|---|---|
| `data/quadras.csv` | todas as quadras cadastradas |
| `data/clientes.csv` | todos os clientes cadastrados |
| `data/reservas.csv` | todas as reservas realizadas |

Ao fechar e abrir o programa novamente, os dados continuam lá.

---

## Erros comuns

| Situação | O que acontece |
|---|---|
| Tentar reservar horário ocupado | Sistema bloqueia e exibe mensagem de erro |
| Digitar ID de cliente que não existe | Sistema avisa que o cliente não foi encontrado |
| Digitar letra onde se espera número | Sistema pede para digitar novamente |
| Campos vazios | Sistema avisa que o campo é obrigatório |
