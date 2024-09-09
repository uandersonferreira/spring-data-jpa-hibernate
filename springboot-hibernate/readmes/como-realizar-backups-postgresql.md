# Guia de Backups e Restaurações no PostgreSQL: Comandos Básicos, Docker e SQL

Este documento oferece um guia prático para realizar backups e restaurações em bancos de dados PostgreSQL. Abrange
comandos básicos via terminal `psql`, SQL e o uso em ambientes Docker.

> Por padrão o postgreSQL quando é instalado ele cria um user default chamado 'postgres'
> use-o para fazer os teste ou crie um novo de sua preferência. Com o docker deve-se usar
> o mesmo user e password informado (recomenda-se criar o nome do user default também)
___
> OBS: Leio o readme de nome 'acessando-arquivos-locais-dentro-de-um-container-docker.md' para compreender melhor
> a parte de restauração.
---

## 1. Comandos Básicos via `psql`

### 1.1. Login no PostgreSQL

Para acessar o PostgreSQL no terminal:

```bash
psql -U seu_user_do_banco
```

### 1.2. Ajuda

Para listar os comandos disponíveis no `psql`:

```bash
\?
```

### 1.3. Sair do `psql`

Para sair do terminal:

```bash
\q
```

### 1.4. Ver Bancos de Dados

Para listar todos os bancos de dados:

```bash
\l
```

### 1.5. Conectar-se a um Banco de Dados

Conecte-se a um banco de dados específico:

```bash
\c nome_base_dados
```

Ou, ao fazer login:

```bash
psql -U seu_user_do_banco -d nome_base_dados
```

### 1.6. Ver Tabelas e Colunas

Para listar as tabelas de um banco de dados:

```bash
\dt
```

Para ver as colunas de uma tabela específica:

```bash
\d nome_tabela
```

---

## 2. Comandos Básicos via SQL

### 2.1. Criar um Banco de Dados

```sql
CREATE
DATABASE nome_database WITH ENCODING = 'UTF-8' OWNER = test CONNECTION LIMIT = 100;
```

### 2.2. Excluir um Banco de Dados

```sql
DROP
DATABASE IF EXISTS nome_database;
```

### 2.3. Ver o Tamanho dos Bancos de Dados

Para listar o tamanho de todos os bancos:

```sql
SELECT pg_database.datname, pg_size_pretty(pg_database_size(pg_database.datname)) AS size
FROM pg_database;
```

### 2.4. Ver as Tabelas Mais Pesadas

Para listar as 10 tabelas que ocupam mais espaço:

```sql
SELECT relname AS "relation", pg_size_pretty(pg_total_relation_size(C.oid)) AS "total_size"
FROM pg_class C
         LEFT JOIN pg_namespace N ON (N.oid = C.relnamespace)
WHERE nspname NOT IN ('pg_catalog', 'information_schema')
  AND C.relkind <> 'i'
  AND nspname !~ '^pg_toast'
ORDER BY pg_total_relation_size(C.oid) DESC LIMIT 10;
```

---

## 3. Comandos de Backup no PostgreSQL

### 3.1. Backup de um Banco de Dados

Para criar um backup do banco:

```bash
pg_dump -U seu_user_do_banco -d nome_database > nome_backup.sql
```

### 3.2. Backup de Todas as Bases de Dados

```bash
pg_dumpall -U seu_user_do_banco > total_backup.sql
```

### 3.3. Backup de uma Tabela Específica

```bash
pg_dump -U seu_user_do_banco -d nome_database -t nome_tabela > backup_tabela.sql
```

### 3.4. Backup Customizado

O PostgreSQL permite realizar backups em diferentes formatos:

- **p**: `plain`
- **c**: `custom`
- **d**: `directory`
- **t**: `tar`

Exemplo:

```bash
pg_dump -U seu_user_do_banco -d nome_database -F c -f backup_customizado.dump
```

### 3.5. Backup com Split (dividido em múltiplos arquivos)

Partir en 2mb

```bash
pg_dump -h localhost -p 5432 -U seu_user_do_banco -d nome_database | split -b 2m – backup.sql
```

---

## 4. Comandos de Restauração no PostgreSQL

### 4.1. Restaurar de um Arquivo `.sql`

Para restaurar um banco a partir de um arquivo `.sql`:

```bash
psql -U seu_user_do_banco -d nome_database < nome_backup.sql
```

### 4.2. Restaurar de um Backup Customizado

Se o backup foi criado em formato customizado:

```bash
pg_restore -U seu_user_do_banco -d nome_database -F c -f backup_customizado.dump
```

### 4.3. Restaurar Backup com Split

Para restaurar um backup dividido em múltiplos arquivos:

```bash
cat backup.sql* | psql -h localhost -p 5432 -U seu_user_do_banco -d nome_database
```

---

## 5. Exemplo de Videoaula: Backup e Restauração

```bash
psql -c "CREATE DATABASE flights;" -U seu_user_do_banco
psql -U seu_user_do_banco -d flights < flights.sql

psql -c "CREATE DATABASE ob_hibernate;" -U seu_user_do_banco
psql -U seu_user_do_banco -d ob_hibernate < ob_hibernate.sql

# Para trabalhar com servidores:
psql -h 127.0.0.1 -p 5432 -U seu_user_do_banco -d demo < backup.sql
```

---

## 6. Executando em um Ambiente Docker

Se o PostgreSQL estiver rodando em um container Docker, você pode realizar backups e restaurações diretamente no
ambiente.

### 6.1. Acessar o Container PostgreSQL

```bash
docker exec -it nome_do_container bash
```

### 6.2. Backup Dentro do Container

```bash
pg_dump -U seu_user_do_banco -d nome_database > /caminho/para/backup.sql
```

### 6.3. Restaurar Backup Dentro do Container

```bash
psql -U seu_user_do_banco -d nome_database < /caminho/para/backup.sql
```

---

## 7. Ferramentas de Backup Adicionais

Além dos comandos básicos, existem ferramentas avançadas para backups e recuperação de desastres em PostgreSQL:

- **Barman**: [GitHub](https://github.com/EnterpriseDB/barman)
- **pg_probackup**: [GitHub](https://github.com/postgrespro/pg_probackup)
- **pgBackRest**: [pgBackRest](https://pgbackrest.org/)
- **pghoard**: [GitHub](https://github.com/aiven/pghoard)

## 8. Bases de dados demo
 - basta pesquisar por: database demo postgreSQL | MySQL

---

