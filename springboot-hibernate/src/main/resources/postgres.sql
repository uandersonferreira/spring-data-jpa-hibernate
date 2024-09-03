-- Seleciona todos os registros da tabela 'employee'.
SELECT * FROM employee;

-- 1. Segurança na Instância
/*
  SUPERUSER: Permissão para realizar qualquer operação no banco de dados.
  CREATEDB: Permissão para criar novos bancos de dados.
  CREATEROLE: Permissão para criar novas funções (roles).
  LOGIN: Permissão para logar no banco de dados.
  REPLICATION: Permissão para replicar dados.
*/

-- Criação de usuários com permissões específicas.
CREATE USER ob_user_1 NOSUPERUSER NOCREATEDB PASSWORD 'admin';
CREATE USER ob_user_2 NOSUPERUSER NOCREATEDB PASSWORD 'test';

-- Concede a função 'ob_user_2' ao usuário 'ob_user_1'.
GRANT ob_user_2 TO ob_user_1;

-- 2. Segurança no Banco de Dados
/*
  CREATE: Permite a criação de objetos dentro do banco de dados.
  CONNECT: Permite a conexão ao banco de dados.
  TEMP/TEMPORARY: Permite a criação de tabelas temporárias.
*/

-- Remove todas as permissões do banco de dados 'db_springboot_hibernate' para o grupo 'public'.
REVOKE ALL ON DATABASE db_springboot_hibernate FROM public;

-- Remove todas as permissões do banco de dados 'db_springboot_hibernate' para o usuário 'ob_user_1'.
REVOKE ALL ON DATABASE db_springboot_hibernate FROM ob_user_1;

-- Concede permissão de conexão ao banco de dados 'db_springboot_hibernate' para 'ob_user_1'.
GRANT CONNECT ON DATABASE db_springboot_hibernate TO ob_user_1;

-- Concede permissão de criação de objetos dentro do banco de dados 'db_springboot_hibernate' para 'ob_user_1'.
GRANT CREATE ON DATABASE db_springboot_hibernate TO ob_user_1;

-- 3. Segurança no Schema
/*
  CREATE: Permite a criação de objetos dentro do schema.
  USAGE: Permite o uso do schema (ex. para consultas).
*/

-- Remove todas as permissões no schema 'public' para 'ob_user_1'.
REVOKE ALL ON SCHEMA public FROM ob_user_1;

-- Concede permissão de criação de objetos no schema 'public' para 'ob_user_1'.
GRANT CREATE ON SCHEMA public TO ob_user_1;

-- Concede permissão de uso do schema 'public' para 'ob_user_1'.
GRANT USAGE ON SCHEMA public TO ob_user_1;

-- 4. Segurança nas Tabelas
/*
	SELECT: Permite selecionar (consultar) dados.
	INSERT: Permite inserir dados.
	UPDATE: Permite atualizar dados.
	DELETE: Permite excluir dados.
	TRUNCATE: Permite truncar (limpar) a tabela.
	TRIGGER: Permite criar gatilhos (triggers).
	REFERENCE: Permite criar chaves estrangeiras que referenciam a tabela.
*/

-- Remove todas as permissões em todas as tabelas no schema 'public' para 'ob_user_1'.
REVOKE ALL ON ALL TABLES IN SCHEMA public FROM ob_user_1;

-- Concede permissão de seleção de dados em todas as tabelas no schema 'public' para 'ob_user_1'.
GRANT SELECT ON ALL TABLES IN SCHEMA public TO ob_user_1;

-- Concede permissão de inserção de dados em todas as tabelas no schema 'public' para 'ob_user_1'.
GRANT INSERT ON ALL TABLES IN SCHEMA public TO ob_user_1;

-- Concede permissão de atualização de dados em todas as tabelas no schema 'public' para 'ob_user_1'.
GRANT UPDATE ON ALL TABLES IN SCHEMA public TO ob_user_1;

-- Concede permissão de exclusão de dados em todas as tabelas no schema 'public' para 'ob_user_1'.
GRANT DELETE ON ALL TABLES IN SCHEMA public TO ob_user_1;

-- 5. Segurança nas Colunas
/*
	SELECT: Permite selecionar (consultar) dados em colunas específicas.
	INSERT: Permite inserir dados em colunas específicas.
	UPDATE: Permite atualizar dados em colunas específicas.
	REFERENCE: Permite que colunas sejam referenciadas por chaves estrangeiras.
*/

-- Tentativa de selecionar todos os registros da tabela 'employee'.
SELECT * FROM employee;

-- Remove a permissão de seleção de dados em todas as tabelas no schema 'public' para 'ob_user_1'.
REVOKE SELECT ON ALL TABLES IN SCHEMA public FROM ob_user_1;

-- Concede permissão de seleção da coluna 'email' na tabela 'employee' para 'ob_user_1'.
GRANT SELECT (email) ON employee TO ob_user_1;

-- 6. Segurança nas Linhas
/*
	A segurança em nível de linha é ativada a nível de tabela e não está ativa por padrão.
	Quando ativada, a política padrão é: Deny all (Negar tudo).
*/

-- Habilita a segurança em nível de linha na tabela 'employee'.
ALTER TABLE employee ENABLE ROW LEVEL SECURITY;

-- Cria uma política que permite a 'ob_user_1' selecionar linhas na tabela 'employee' onde a idade (age) é maior que 18.
CREATE POLICY employee_gte_18 ON employee
FOR SELECT
               TO ob_user_1
               USING (age > 18);

-- Verificação de privilégios
-- Verifica se 'db_springboot_hibernate' possui privilégios de 'CREATE'.
-- Verifica se 'public' possui privilégios de 'CREATE' e 'USAGE'.
-- Verifica se 'employee' possui privilégios de 'SELECT' e 'INSERT'.
-- Verifica se a tabela 'employee' possui qualquer privilégio em colunas.
SELECT has_database_privilege('db_springboot_hibernate', 'CREATE'),
       has_schema_privilege('public', 'CREATE'),
       has_schema_privilege('public', 'USAGE'),
       has_table_privilege('employee', 'SELECT'),
       has_table_privilege('employee', 'INSERT'),
       has_any_column_privilege('employee', 'SELECT');

-- Verifica se 'ob_user_1' possui privilégio de 'SELECT' na coluna 'age' da tabela 'employee'.
SELECT has_column_privilege('ob_user_1', 'employee', 'age', 'SELECT');

-- Verifica se 'ob_user_1' possui privilégio de 'SELECT' na coluna 'email' da tabela 'employee'.
SELECT has_column_privilege('ob_user_1', 'employee', 'email', 'SELECT');
