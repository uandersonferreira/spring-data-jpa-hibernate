# Hibernate

## Introduction

1. Java Database Connectivity (JDBC)

2. JPA (Java Persistence API) Specification (javax.persistence mudou para jakarta.persistence)
    - Implementations:
        - Hibernate (ORM object Relational Mapping)
        - EclipseLink

3. Spring Data JPA
    - CRUD: Create | Retrieve | Update | Delete

## Baixar base de dados e ferramentas graficas de visualização dos dados

- Mysql: https://dev.mysql.com/downloads/installer/
- MariaDB: https://mariadb.org/download
- Postgresql: https://postgresql.org/download/
- Dbeaver UI: https://dbeaver.io

## Outra Opção - Baixar base de dados Mysql via Docker-compose

```dockerfile
version: '3'
services:

  ### MySQL database for Hibernate example
  db-ob_hibernate:
    container_name: ob_hibernate
    image: mysql
    environment:
      MYSQL_DATABASE: ob_hibernate
      MYSQL_USER: test
      MYSQL_PASSWORD: test123
      MYSQL_ROOT_PASSWORD: root # Senha do usuário root
    ports:
      - "3306:3306"
    expose:
      - 3306
    volumes:
      - db-ob_hibernate:/var/lib/mysql

volumes:
  db-ob_hibernate: #Volume para dados

     
```

## Configuration

1. Adicionar as dependências no arquivo `pom.xml`:

**hibernate-core**
**Driver da base de dados:** mysql, postgresql, etc. (O que achar melhor)

2. Criar o arquivo `hibernate.cfg.xml` no diretório `src/main/resources` e definir sua configuração

3. Criar o objeto SessionFactory do Java

4. Criar modelos/Entidades e realizar operações de CRUD

## Operações de CRUD

Create, Retrieve, Update, Delete

### Operações de Leitura (Retrieve)

```sql
SELECT *
FROM table_name...
```

### Operações de escrita (Create, Update, Delete)

**Create:**

```sql
INSERT INTO table_name (col1, col2, ...)
values (value1, value2, ...);
```

**Update:**

```sql
UPDATE table_name
SET column1 = value1,
    column2 = value2, ...
    WHERE condition;
```

**Delete:**

```sql
DELETE
FROM table_name
WHERE condition;
```

## Onde se programa o código que interage com a base de dados:

Em classes Repository ou DAO

Esquema geral de uma aplicação web com Hibernate:

``` 
Navegador --> Controller --> Service --> DAO ou Repository --> Base de dados (Mysql, PostgreSQL, H2, ...)
```

## Preenchendo a base de dados

Hibernate ejecuta pro default o arquivo `import.sql` se o mesmo existir.

Com a propriedade `hibernate.hbm2ddl.import_files` podemos indicar ao Hibernate que ejecute mais de um
arquivo SQL.
ex:

```
 <property name="hibernate.hbm2ddl.import_files">import.sql, data.sql</property> 
```

E também podemos inserir novos dados apartir do Java ao utilizar os metódos da interface `Session`.

```
 SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.persist(employee1);//save-@Deprecated(since = "6.0") usar persist
        session.persist(employee2);//save-@Deprecated(since = "6.0")

        session.getTransaction().commit();

        session.close();
        sessionFactory.close();
        HibernateUtil.shutdown();
```

## Associações entre Entidades

* @OneToOne
    * @OneToOne
    * @JoinColumn
    * @JoinTable
    * @PrimaryKeyJoinColumn
    * @MapsId


### 1. **@OneToOne**

Essa anotação define uma associação de um-para-um entre duas entidades. Em um relacionamento `@OneToOne`, uma instância
de uma entidade está associada a uma única instância de outra entidade.

Exemplo conceitual: Um empregado pode ter exatamente uma direção (endereço), e um endereço pertence exatamente a um
empregado.

### 2. **@JoinColumn**

Essa anotação é usada junto com `@OneToOne` para especificar a coluna de junção (chave estrangeira) na tabela da
entidade "dona" da relação. Ela define explicitamente qual coluna na tabela do banco de dados contém a chave estrangeira
que referencia a outra entidade.

**Conceito:** A coluna da chave estrangeira que relaciona uma entidade com outra fica na tabela da entidade "dona" do
relacionamento.

Exemplo conceitual: Se a entidade `Employee` é a dona da relação, a tabela `employee` terá uma coluna `address_id` que
referencia a tabela `address`.

### 3. **@JoinTable**

`@JoinTable` é usada quando você deseja especificar uma tabela de junção explicitamente. Em vez de colocar a chave
estrangeira diretamente em uma das tabelas envolvidas no relacionamento, uma terceira tabela (tabela de junção) é
criada. Esta tabela contém as chaves estrangeiras de ambas as entidades.

**Conceito:** Uma tabela intermediária (tabela de junção) é usada para armazenar as chaves estrangeiras das duas
entidades.

Exemplo conceitual: Para o relacionamento entre `Employee` e `Address`, você teria uma tabela de junção,
como `employee_address`, contendo as colunas `employee_id` e `address_id`.

### 4. **@PrimaryKeyJoinColumn**

Essa anotação é usada em associações `@OneToOne` onde ambas as entidades compartilham a mesma chave primária. Isso
geralmente significa que a chave primária da entidade "dona" da relação é também a chave estrangeira que referencia a
entidade associada.

**Conceito:** A chave primária da entidade dona é usada como a chave estrangeira para referenciar a entidade associada.

Exemplo conceitual: A entidade `Employee` e `Address` compartilham a mesma chave primária. Se o `employee_id` for 1,
o `address_id` também será 1.

### 5. **@MapsId**

`@MapsId` é usado para mapear a chave primária de uma entidade associada diretamente à chave primária de outra entidade,
sem a necessidade de duplicação. Isso pode ser útil em associações `@OneToOne` onde você quer que a chave primária de
uma entidade seja usada também como a chave estrangeira para a outra entidade.

**Conceito:** A chave primária de uma entidade é reutilizada como a chave estrangeira para a entidade relacionada.

Exemplo conceitual: A chave primária de `Employee` é mapeada diretamente para a chave primária de `Address`, e não há
necessidade de uma chave estrangeira separada.

