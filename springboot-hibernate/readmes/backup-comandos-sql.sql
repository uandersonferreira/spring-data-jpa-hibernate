-- Como realizar backups no PostgreSQL: Comandos básicos SQL

-- Criar um banco de dados chamado "prueba" com codificação UTF-8,
-- proprietário "postgres" e limite de 100 conexões
CREATE DATABASE prueba
WITH ENCODING = 'UTF-8'
OWNER = postgres
CONNECTION LIMIT = 100;

-- Excluir os bancos de dados "prueba" e "demo", se existirem
DROP DATABASE IF EXISTS prueba;
DROP DATABASE IF EXISTS demo;

-- Ver o tamanho de todas as bases de dados no servidor
SELECT pg_database.datname,
       pg_size_pretty(pg_database_size(pg_database.datname)) AS size
FROM pg_database;

-- Ver o tamanho de uma base de dados específica chamada "db_springboot_hibernate"
SELECT pg_size_pretty(pg_database_size('db_springboot_hibernate'));

-- Ver as 10 tabelas que ocupam mais espaço no banco de dados atual
SELECT relname AS "relation",
       pg_size_pretty(pg_total_relation_size(C.oid)) AS "total_size"
FROM pg_class C
         LEFT JOIN pg_namespace N ON (N.oid = C.relnamespace)
WHERE nspname NOT IN ('pg_catalog', 'information_schema')  -- Ignorar tabelas do sistema
  AND C.relkind <> 'i'  -- Excluir índices
  AND nspname !~ '^pg_toast'  -- Excluir tabelas de armazenamento interno
ORDER BY pg_total_relation_size(C.oid) DESC
    LIMIT 10;
