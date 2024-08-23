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