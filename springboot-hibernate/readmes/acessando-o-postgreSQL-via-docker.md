# Acessando o Banco de Dados PostgreSQL no Docker

Este guia explica como acessar um banco de dados PostgreSQL que está sendo executado em um container Docker. O processo não requer a instalação do PostgreSQL ou do cliente `psql` no seu sistema local, já que todas as interações ocorrem dentro do próprio container.

## Pré-requisitos

- Docker instalado e configurado no sistema.
- Um container Docker rodando PostgreSQL.
- Acesso ao terminal do sistema operacional (Linux, macOS ou WSL para Windows).

## Passo a Passo

### 1. Verifique se o container PostgreSQL está rodando

Antes de acessar o banco de dados, é importante garantir que o container está em execução.

1. Abra o terminal.
2. Execute o seguinte comando para listar todos os containers em execução:

   ```bash
   docker ps
   ```

   Verifique se o container que hospeda o PostgreSQL está ativo. O nome do container deve ser exibido na coluna `NAMES`. Exemplo de nome: `db_springboot_hibernate`.

### 2. Acesse o terminal do container

Para acessar o PostgreSQL, você deve primeiro entrar no terminal do container Docker:

1. Execute o seguinte comando para abrir um terminal interativo dentro do container:

   ```bash
   docker exec -it <container_name> bash
   ```

   Substitua `<container_name>` pelo nome do seu container, que você identificou no comando anterior. Por exemplo:

   ```bash
   docker exec -it db_springboot_hibernate bash
   ```

### 3. Acesse o PostgreSQL usando o cliente `psql`

Dentro do container, use o cliente `psql` para acessar o banco de dados PostgreSQL:

1. Execute o comando abaixo para se conectar ao banco de dados:

   ```bash
   psql -U <username> -d <database_name>
   ```

   Onde:
    - `-U <username>`: especifica o usuário do banco de dados.
    - `-d <database_name>`: nome do banco de dados que você deseja acessar.

   Exemplo de comando:

   ```bash
   psql -U test -d db_springboot_hibernate
   ```

   O sistema solicitará a senha do usuário, se configurada. Neste exemplo, a senha pode ser fornecida no arquivo Docker Compose (`POSTGRES_PASSWORD`).

### 4. Interaja com o banco de dados

Após conectar-se ao PostgreSQL, você pode executar consultas SQL ou gerenciar o banco de dados. Alguns exemplos de comandos SQL:

- Ver todas as tabelas do banco:

  ```sql
  \dt
  ```

- Ver os detalhes da tabela `employees`:

  ```sql
  \d employees
  ```

- Fazer uma consulta:

  ```sql
  SELECT * FROM employees;
  ```

### 5. Finalizar sessão

Para sair da interface do `psql`, execute o comando:

```bash
\q
```

Para sair do terminal do container, basta digitar:

```bash
exit
```

## Resumo dos Comandos

- Verificar se o container está rodando:

  ```bash
  docker ps
  ```

- Acessar o container:

  ```bash
  docker exec -it <container_name> bash
  ```

- Acessar o PostgreSQL dentro do container:

  ```bash
  psql -U <username> -d <database_name>
  ```

- Sair do `psql`:

  ```bash
  \q
  ```

- Sair do terminal do container:

  ```bash
  exit
  ```

---

### Observações

- Certifique-se de que o nome do container, o nome do usuário e o nome do banco de dados estão corretos e de acordo com as configurações do seu Docker Compose ou ambiente.
- Se houver problemas de conexão, verifique o mapeamento de portas no Docker Compose (geralmente a porta 5432) e as permissões de rede.

---

