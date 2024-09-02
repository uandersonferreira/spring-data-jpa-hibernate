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

* One To One - Employee and Direction
    * @OneToOne
    * @JoinColumn
    * @JoinTable
    * @PrimaryKeyJoinColumn
    * @MapsId
  
* One To Many - Employee and Car
  * unidirecional
  * bidirecional
  
* Many To One - Employee and Company
    * unidirecional
    * bidirecional
  
* Many To Many - Employee and Project
    * unidirecional
    * bidirecional


> Consulte os readmes do projeto paar saber mais

### 1. **@OneToOne**

Essa anotação define uma associação de um-para-um entre duas entidades. Em um relacionamento `@OneToOne`, uma instância
de uma entidade está associada a uma única instância de outra entidade.

Exemplo conceitual: Um empregado pode ter exatamente uma direção (endereço), e um endereço pertence exatamente a um
empregado.

### 1.2. **@JoinColumn**

Essa anotação é usada junto com `@OneToOne` para especificar a coluna de junção (chave estrangeira) na tabela da
entidade "dona" da relação. Ela define explicitamente qual coluna na tabela do banco de dados contém a chave estrangeira
que referencia a outra entidade.

**Conceito:** A coluna da chave estrangeira que relaciona uma entidade com outra fica na tabela da entidade "dona" do
relacionamento.

Exemplo conceitual: Se a entidade `Employee` é a dona da relação, a tabela `employee` terá uma coluna `address_id` que
referencia a tabela `address`.

### 1.3. **@JoinTable**

`@JoinTable` é usada quando você deseja especificar uma tabela de junção explicitamente. Em vez de colocar a chave
estrangeira diretamente em uma das tabelas envolvidas no relacionamento, uma terceira tabela (tabela de junção) é
criada. Esta tabela contém as chaves estrangeiras de ambas as entidades.

**Conceito:** Uma tabela intermediária (tabela de junção) é usada para armazenar as chaves estrangeiras das duas
entidades.

Exemplo conceitual: Para o relacionamento entre `Employee` e `Address`, você teria uma tabela de junção,
como `employee_address`, contendo as colunas `employee_id` e `address_id`.

### 1.4. **@PrimaryKeyJoinColumn**

Essa anotação é usada em associações `@OneToOne` onde ambas as entidades compartilham a mesma chave primária. Isso
geralmente significa que a chave primária da entidade "dona" da relação é também a chave estrangeira que referencia a
entidade associada.

**Conceito:** A chave primária da entidade dona é usada como a chave estrangeira para referenciar a entidade associada.

Exemplo conceitual: A entidade `Employee` e `Address` compartilham a mesma chave primária. Se o `employee_id` for 1,
o `address_id` também será 1.

### 1.5. **@MapsId**

`@MapsId` é usado para mapear a chave primária de uma entidade associada diretamente à chave primária de outra entidade,
sem a necessidade de duplicação. Isso pode ser útil em associações `@OneToOne` onde você quer que a chave primária de
uma entidade seja usada também como a chave estrangeira para a outra entidade.

**Conceito:** A chave primária de uma entidade é reutilizada como a chave estrangeira para a entidade relacionada.

Exemplo conceitual: A chave primária de `Employee` é mapeada diretamente para a chave primária de `Address`, e não há
necessidade de uma chave estrangeira separada.



### 2. **One To Many - Employee and Car**

#### Unidirecional

- **Conceito:** No relacionamento unidirecional `OneToMany`, a classe "dona" (que possui a coleção) conhece a classe associada, mas a classe associada não tem referência de volta para a classe "dona".
- **Definição:** Um `Employee` pode possuir vários `Car`, mas um `Car` não tem conhecimento sobre o `Employee` ao qual pertence.

#### Bidirecional

- **Conceito:** No relacionamento bidirecional `OneToMany`, ambas as classes conhecem uma à outra. A classe "dona" possui uma coleção da outra classe, e a classe associada tem uma referência de volta para a classe "dona".
- **Definição:** Um `Employee` pode possuir vários `Car` e, ao mesmo tempo, cada `Car` sabe a qual `Employee` pertence.

### 3. **Many To One - Employee and Company**

#### Unidirecional

- **Conceito:** No relacionamento unidirecional `ManyToOne`, a classe "dona" (que possui a referência) conhece a classe associada, mas a classe associada não tem conhecimento da classe "dona".
- **Definição:** Vários `Employee` podem estar associados a um único `Company`, mas um `Company` não tem conhecimento direto de todos os `Employee` associados a ele.

#### Bidirecional

- **Conceito:** No relacionamento bidirecional `ManyToOne`, tanto a classe "dona" quanto a classe associada conhecem uma à outra. A classe "dona" possui uma referência para a classe associada e, ao mesmo tempo, a classe associada mantém uma coleção ou uma lista de referências para a classe "dona".
- **Definição:** Vários `Employee` podem estar associados a um único `Company`, e o `Company` pode manter uma lista de todos os `Employee` que pertencem a ele.

### 4. **Many To Many - Employee and Project**

#### Unidirecional

- **Conceito:** No relacionamento unidirecional `ManyToMany`, a classe "dona" (que possui a coleção) conhece a classe associada, mas a classe associada não tem conhecimento da classe "dona".
- **Definição:** Um `Employee` pode estar associado a vários `Project`, mas um `Project` não tem conhecimento sobre os `Employee` associados a ele.

#### Bidirecional

- **Conceito:** No relacionamento bidirecional `ManyToMany`, ambas as classes conhecem uma à outra. A classe "dona" possui uma coleção da classe associada, e a classe associada possui uma coleção ou lista da classe "dona".
- **Definição:** Um `Employee` pode estar associado a vários `Project`, e um `Project` pode ter vários `Employee` associados a ele.

## Consultas 

* Consultas:
  * HQL
  * Natives SQL
  * NamedQueries
  * Criteria API
  * Métodos Hibernates/JPA: find, save, persist, delete, remove
  
## Gestão de Eventos

* Lifecycle callbacks Events: prePersist, preUpdate, preRemove, postPersist...
* @Audited: a nível de class / atributo
* Interceptores

## Migração de cambios de esquema en la base de datos

* liquibase: https://www.liquibase.com/

* flyway: https://www.red-gate.com/products/flyway/community/

