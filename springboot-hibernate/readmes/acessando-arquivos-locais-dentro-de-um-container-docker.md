# Tutorial: Acessando Arquivos Locais dentro de um Container Docker

Este tutorial explica como montar um diretório local no seu container Docker para que arquivos do seu PC possam ser
acessados e utilizados dentro do container. Este processo será útil em cenários como restaurar bancos de dados a partir
de arquivos `.sql` ou qualquer outro tipo de transferência de dados.

## Pré-requisitos

1. Docker instalado e em execução.
2. Um container PostgreSQL já configurado (ou qualquer outro serviço).
3. Acesso a um terminal de comandos no seu sistema operacional.

## Passo 1: Preparando o Arquivo Local

Vamos supor que você tenha um arquivo de backup do PostgreSQL chamado `flights.sql` que deseja restaurar dentro de um
container PostgreSQL.

1. **Criar um diretório para compartilhar com o container:**

   No seu PC local, crie um diretório onde seus arquivos serão armazenados. Isso será o diretório que você montará no
   container Docker:

   ```bash
   mkdir -p /home/usuario/backup
   ```

   *Substitua `/home/usuario/backup` pelo caminho desejado no seu sistema*.

2. **Mover o arquivo para o diretório:**

   Agora, mova o arquivo que você deseja acessar no container para esse diretório (ou copie e cole):

   ```bash
   mv /caminho/para/seu/arquivo/flights.sql /home/usuario/backup/
   ```

   Agora você tem o arquivo `flights.sql` localizado dentro do diretório `/home/usuario/backup`.

## Passo 2: Atualizando o `docker-compose.yml`

O próximo passo é garantir que o seu arquivo `docker-compose.yml` esteja configurado para montar o diretório local
dentro do container.

1. **Abra seu arquivo `docker-compose.yml`:**

   Adicione ou ajuste a configuração para incluir o volume local que você acabou de criar. Aqui está um exemplo:

   ```yaml
   version: '3'
   services:
     db:
       container_name: db_postgres
       image: postgres:latest
       environment:
         POSTGRES_DB: nome_do_banco_de_dados
         POSTGRES_USER: usuario
         POSTGRES_PASSWORD: senha
       ports:
         - "5432:5432"
       volumes:
         - volumes-data:/var/lib/postgresql/data
         - /home/usuario/backup:/backup # Montando diretório local no container

   volumes:
     volumes-data:
   ```

   Nesta configuração, estamos mapeando o diretório local `/home/usuario/backup` para o diretório `/backup` dentro do
   container.

2. **Salvar e reiniciar o container:**

   Agora que o arquivo `docker-compose.yml` foi atualizado, você precisa reiniciar o container para aplicar as mudanças:

   ```bash
   docker-compose down
   docker-compose up -d
   ```

## Passo 3: Verificando o Arquivo no Container

Agora que o container foi reiniciado com o volume montado, você pode acessar o arquivo dentro do container.

1. **Acesse o container via terminal:**

   Use o seguinte comando para acessar o terminal do container:

   ```bash
   docker exec -it db_postgres bash
   ```

   *Substitua `db_postgres` pelo nome do seu container.*

2. **Navegar até o diretório montado:**

   Dentro do terminal do container, navegue até o diretório `/backup`:

   ```bash
   cd /backup
   ```

   Liste os arquivos para verificar se o arquivo `flights.sql` está presente:

   ```bash
   ls
   ```

   Se tudo estiver correto, o arquivo `flights.sql` aparecerá listado.

## Passo 4: Restaurando o Banco de Dados

Agora que o arquivo está acessível dentro do container, você pode restaurá-lo no banco de dados PostgreSQL.

1. **Restaurar o arquivo SQL no banco de dados:**

   Dentro do container, execute o seguinte comando para restaurar o banco de dados:

   ```bash
   psql -U usuario -d nome_do_banco_de_dados -f /backup/flights.sql
   ```

   *Substitua `usuario` pelo nome de usuário do PostgreSQL e `nome_do_banco_de_dados` pelo nome do banco de dados que
   você está usando*.

## Passo 5: Finalizando

Agora, o banco de dados foi restaurado a partir do arquivo SQL. Você pode sair do container com o comando:

```bash
exit
```

### Resumo dos Comandos

1. **Criar o diretório local:**
   ```bash
   mkdir -p /home/usuario/backup
   ```

2. **Mover o arquivo para o diretório:**
   ```bash
   mv /caminho/para/seu/arquivo/flights.sql /home/usuario/backup/
   ```

3. **Atualizar o `docker-compose.yml` para montar o volume:**
   ```yaml
   volumes:
     - /home/usuario/backup:/backup
   ```

4. **Reiniciar o container:**
   ```bash
   docker-compose down
   docker-compose up -d
   ```

5. **Acessar o container e o arquivo:**
   ```bash
   docker exec -it db_postgres bash
   cd /backup
   ls
   ```

6. **Restaurar o banco de dados:**
   ```bash
   psql -U usuario -d nome_do_banco_de_dados -f /backup/flights.sql
   ```

---

