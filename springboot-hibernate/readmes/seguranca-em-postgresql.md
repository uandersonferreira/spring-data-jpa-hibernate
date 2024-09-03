
## Segurança no PostgreSQL

A segurança no PostgreSQL é gerenciada por meio de funções (roles). Usuários, funções e grupos são tratados de forma semelhante, e a maneira como são utilizados define como são mencionados.

### Níveis de Segurança

1. **Instância**: Gerenciamento de usuários, funções, criação de bancos de dados, controle de login e replicação.
2. **Banco de Dados**: Controle de conexão, criação de esquemas, e outras operações relacionadas ao banco de dados.
3. **Schema**: Permissões para usar o schema e criar objetos dentro dele.
4. **Tabela**: Controle sobre operações de Linguagem de Manipulação de Dados (DML) como SELECT, INSERT, UPDATE, DELETE, etc.
5. **Colunas**: Permissão ou restrição de acesso a colunas específicas dentro de uma tabela.
6. **Linhas**: Restringe o acesso a linhas específicas dentro de uma tabela, baseado em políticas definidas.


---

**1. Segurança da Instância**

```sql
/*
  SUPERUSER: Concede privilégios de superusuário.
  CREATEDB: Permite criar bancos de dados.
  CREATEROLE: Permite criar novas funções (roles).
  LOGIN: Permite logar no banco de dados.
  REPLICATION: Permite replicar dados (normalmente usado para backups).
*/
create user ob_user_1 NOSUPERUSER NOCREATEDB PASSWORD 'admin';
create user ob_user_2 NOSUPERUSER NOCREATEDB PASSWORD 'test';

grant ob_user_2 to ob_user_1;
```

- **create user**: Cria um novo usuário com as permissões especificadas. Neste caso, `NOSUPERUSER` impede que o usuário tenha privilégios de superusuário, e `NOCREATEDB` impede que ele crie bancos de dados.
- **grant**: Concede uma função (`ob_user_2`) ao usuário (`ob_user_1`).

**2. Segurança do Banco de Dados**

```sql
/*
  CREATE: Permite criar objetos dentro do banco de dados.
  CONNECT: Permite que o usuário se conecte ao banco de dados.
  TEMP/TEMPORARY: Permite criar tabelas temporárias.
*/
REVOKE ALL ON DATABASE db_springboot_hibernate FROM public; -- remove todas as permissões para dar permissões personalizadas
REVOKE ALL ON DATABASE db_springboot_hibernate FROM ob_user_1;

GRANT CONNECT ON DATABASE db_springboot_hibernate to ob_user_1;
GRANT CREATE ON DATABASE db_springboot_hibernate to ob_user_1;
```

- **REVOKE**: Remove todas as permissões do banco de dados (`db_springboot_hibernate`) para `public` e para `ob_user_1`.
- **GRANT**: Concede permissões específicas para o banco de dados, como `CONNECT` e `CREATE`, ao usuário `ob_user_1`.

**3. Segurança do Schema**

```sql
/*
  CREATE: Permite criar objetos dentro do schema.
  USAGE: Permite o uso do schema.
*/
REVOKE ALL ON SCHEMA public FROM ob_user_1;

GRANT CREATE ON SCHEMA public to ob_user_1;
GRANT USAGE ON SCHEMA public to ob_user_1;
```

- **REVOKE**: Remove todas as permissões do schema `public` para `ob_user_1`.
- **GRANT**: Concede permissões para criar (`CREATE`) e usar (`USAGE`) o schema `public` para `ob_user_1`.

**4. Segurança das Tabelas**

```sql
/*
	SELECT: Permite selecionar dados.
	INSERT: Permite inserir dados.
	UPDATE: Permite atualizar dados.
	DELETE: Permite excluir dados.
	TRUNCATE: Permite truncar (apagar todos os dados) de uma tabela.
	TRIGGER: Permite criar gatilhos.
	REFERENCE: Permite criar chaves estrangeiras que referenciam as tabelas.
*/
REVOKE ALL ON ALL TABLES IN SCHEMA public FROM ob_user_1;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO ob_user_1;
GRANT INSERT ON ALL TABLES IN SCHEMA public TO ob_user_1;
GRANT UPDATE ON ALL TABLES IN SCHEMA public TO ob_user_1;
GRANT DELETE ON ALL TABLES IN SCHEMA public TO ob_user_1;
```

- **REVOKE**: Remove todas as permissões sobre todas as tabelas no schema `public` para `ob_user_1`.
- **GRANT**: Concede permissões para `SELECT`, `INSERT`, `UPDATE` e `DELETE` em todas as tabelas no schema `public` para `ob_user_1`.

**5. Segurança das Colunas**

```sql
/*
	SELECT: Permite selecionar dados de colunas específicas.
	INSERT: Permite inserir dados em colunas específicas.
	UPDATE: Permite atualizar dados em colunas específicas.
	REFERENCE: Permite que colunas sejam referenciadas por chaves estrangeiras.
*/
SELECT * FORM employee;  -- Deve ser `FROM` ao invés de `FORM`

REVOKE SELECT ON ALL TABLES IN SCHEMA public FROM ob_user_1;

GRANT SELECT (email) ON employee TO ob_user_1;
```

- **SELECT * FORM employee**: Deve ser corrigido para `SELECT * FROM employee;`.
- **REVOKE**: Remove a permissão de `SELECT` em todas as tabelas no schema `public` para `ob_user_1`.
- **GRANT**: Concede permissão para selecionar a coluna `email` na tabela `employee` para `ob_user_1`.

**6. Segurança das Linhas**

```sql
/*
	Se habilita a nível de tabela, não está ativo por padrão.
	Uma vez habilitado, o valor padrão é: Deny all (Negar tudo).
*/
ALTER TABLE employee ENABLE ROW LEVEL SECURITY;

CREATE POLICY employee_gte_18 ON employee
FOR SELECT
TO ob_user_1
USING (age > 18);
```

- **ALTER TABLE**: Habilita a segurança em nível de linha na tabela `employee`.
- **CREATE POLICY**: Cria uma política de segurança em nível de linha que permite que `ob_user_1` selecione somente as linhas onde `age` é maior que 18.

**Verificação de Privilegios**

```sql
SELECT 
has_database_privilege('db_springboot_hibernate', 'CREATE'),
has_schema_privilege('public', 'CREATE'),
has_schema_privilege('public', 'USAGE'),
has_table_privilege('employee', 'SELECT'),
has_table_privilege('employee', 'INSERT'),
has_any_column_privilege('employee', 'SELECT');

SELECT has_column_privilege('ob_user_1', 'employee', 'age', 'SELECT');

SELECT has_column_privilege('ob_user_1', 'employee', 'email', 'SELECT');
```

- **has_database_privilege**: Verifica se o usuário tem privilégio de `CREATE` no banco de dados especificado.
- **has_schema_privilege**: Verifica se o usuário tem os privilégios `CREATE` ou `USAGE` no schema especificado.
- **has_table_privilege**: Verifica se o usuário tem privilégios em uma tabela específica.
- **has_any_column_privilege**: Verifica se o usuário tem qualquer privilégio em uma coluna de uma tabela.
- **has_column_privilege**: Verifica se o usuário tem privilégio em uma coluna específica de uma tabela.




