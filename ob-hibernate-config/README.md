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
