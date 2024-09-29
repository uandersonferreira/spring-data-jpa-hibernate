**Aqui está a tradução para o português:

# Replicação no PostgreSQL

**Requisitos:** ter o Docker instalado.  
**Recomendação:** sistema operacional Linux.

**Instalação do Docker no Linux:** [Docker Installation Guide](https://docs.docker.com/engine/install/ubuntu/)

```bash
docker stop server1 server2

docker rm server1 server2

docker run -itd --name=server1 --net host ubuntu:20.04

docker run -itd --name=server2 --net host ubuntu:20.04
```

## Configuração do server1

```bash
docker exec -it server1 bash

apt-get update

apt-get upgrade

apt-get install lsb-release wget nano postgresql -y

service postgresql status

service postgresql start

su postgres

psql

\password postgres
```

Nos pede a senha e atribuímos a que quisermos, por exemplo: admin

```bash
create database sales;

\c sales

CREATE TABLE guestbook (visitor_email text, visitor_id serial, date timestamp, message text);

INSERT INTO guestbook (visitor_email, date, message) VALUES ('jim@gmail.com', current_date, 'Este é um teste.');

CREATE USER replication REPLICATION LOGIN CONNECTION LIMIT 5 ENCRYPTED PASSWORD 'admin';

\q 
```

Aqui está a versão aprimorada com as informações sobre replicação no PostgreSQL:

### 1. **pg_hba.conf**

- **Propósito:** Controla o acesso dos clientes ao banco de dados. Este arquivo especifica quais usuários podem se
  conectar a quais bancos de dados a partir de quais endereços IP, além do método de autenticação que deve ser
  utilizado. É fundamental para configurar a segurança do acesso, especialmente em ambientes de replicação, onde é
  necessário garantir que apenas usuários autorizados possam se conectar como replicadores.

- **Configurações Principais:**
    - **Host:** Define se a conexão é feita através de um socket Unix ou uma rede (IPv4/IPv6).
    - **Database:** O banco de dados ao qual o usuário está tentando se conectar.
    - **User:** O nome do usuário do banco de dados que está tentando se conectar.
    - **Address:** O endereço IP ou intervalo de endereços que pode se conectar.
    - **Method:** O método de autenticação (por exemplo, `trust`, `md5`, `scram-sha-256`, etc.).

- **Localização:** Normalmente encontrado em `/etc/postgresql/[versão]/main/pg_hba.conf`.

#### Exemplo de configuração:

```bash
nano /etc/postgresql/12/main/pg_hba.conf
```

Adicionar no final a seguinte linha para permitir conexões de replicação:

```
host    replication     replication     127.0.0.1/32            md5
```

Isso garante que o usuário de replicação possa se conectar ao servidor primário para a replicação.

### 2. **postgresql.conf**

- **Propósito:** Configura as opções globais do servidor PostgreSQL, incluindo ajustes de desempenho, parâmetros de
  conexão e várias opções operacionais que afetam o comportamento do servidor. No contexto da replicação, é crucial
  configurar corretamente os parâmetros que controlam a comunicação entre o servidor primário e os servidores
  secundários.

- **Configurações Principais:**
    - **listen_addresses:** Define em quais endereços IP o servidor deve escutar (por exemplo, `localhost`, `*` para
      todos os endereços). Isso é importante para permitir que clientes se conectem ao servidor.
    - **port:** O número da porta na qual o servidor escuta (por padrão, 5432).
    - **wal_level:** Define o nível de detalhamento dos logs de Write-Ahead Logging (WAL). Para replicação, deve ser
      configurado como `'replica'`.
    - **archive_mode:** Habilita o modo de arquivamento de WAL, que é essencial para replicação.
    - **archive_command:** Comando a ser executado para arquivar os logs.
    - **max_wal_senders:** O número máximo de processos que podem enviar dados de WAL para clientes. Importante para
      suportar várias conexões de replicação.
    - **primary_conninfo:** Informações de conexão para o servidor primário, usadas pelos servidores secundários para se
      conectarem ao primário.
    - **hot_standby:** Permite consultas em um servidor secundário, permitindo que ele funcione como um servidor de
      leitura enquanto ainda está replicando.

- **Localização:** Normalmente encontrado em `/etc/postgresql/[versão]/main/postgresql.conf`.

#### Exemplo de configuração:

```bash
nano /etc/postgresql/12/main/postgresql.conf
```

Configurações relevantes para replicação:

```plaintext
# CONFIGS REPLICATION BY AULA
listen_addresses = 'localhost,127.0.0.1'           
wal_level = 'replica'
archive_mode = on
archive_command = 'cd .'
max_wal_senders = 5
primary_conninfo = 'host=127.0.0.1 port=5433 user=replication password=admin'
hot_standby = on

service postgresql restart
```

### 3. **Configuração do server2**

A configuração do servidor secundário deve seguir um processo similar, garantindo que a replicação funcione
corretamente.

#### Comandos para configuração:

```bash
docker exec -it server2 bash

apt-get update

apt-get upgrade

apt-get install lsb-release wget nano postgresql -y
```

Configurar `pg_hba.conf`:

```bash
nano /etc/postgresql/12/main/pg_hba.conf
```

Adicionar:

```
host    replication     replication     127.0.0.1/32            md5
```

Configurar `postgresql.conf`:

```bash
nano /etc/postgresql/12/main/postgresql.conf
```

Alterar a porta e adicionar configurações para replicação:

```plaintext
port = 5433

# CONFIGS REPLICATION BY AULA
listen_addresses = 'localhost,127.0.0.1'           
wal_level = 'replica'
archive_mode = on
archive_command = 'cd .'
max_wal_senders = 5
primary_conninfo = 'host=127.0.0.1 port=5432 user=replication password=admin'
hot_standby = on

service postgresql restart

```

#### Backup e inicialização do server2:

```bash
su postgres

mv /var/lib/postgresql/12/main /var/lib/postgresql/12/main_old

pg_basebackup -h 127.0.0.1 -D /var/lib/postgresql/12/main -U replication -P -v

```

### 4. **Iniciar o processo de replicação do server 2**

Para iniciar o processo de replicação no servidor secundário, execute:

```bash
touch /var/lib/postgresql/12/main/standby.signal

service postgresql restart
```

Caso tenha o erro parecido com isso:

```bash
FATAL:  database system identifier differs between the primary and standby
DETAIL:  The primary's identifier is 7420152808191545960, the standby's identifier is 7420161306155444204.
```
Remova o Diretório de Dados do Standby do server 2

Sendo assim, antes de realizar um novo backup, remova o diretório de dados do
servidor secundário que você criou :

```bash
rm -rf /var/lib/postgresql/12/main
```
### 5. **Verificação**

Após a configuração e inicialização, verifique se os mesmos dados aparecem em ambas as instalações do PostgreSQL para
confirmar que a replicação está funcionando corretamente.
