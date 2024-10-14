# **JPA / HIBERNATE INTRODUÇÃO**

## **ORM** (Object Relational Mapping)

Mapeamento objeto-relacional ou ORM é a técnica de programação para
mapear objetos do modelo de domínio do aplicativo (class Java) para as
tabelas do banco de dados relacional. O conceito fundamental por trás do
ORM é como os objetos se relacionam entre si e como se representa essas
relações como entidades em bancos de dados.

De forma resumida, ele atua como uma ponte/intermediador, que fornece uma camada que relaciona os
dados de um modelo baseado em objeto com entidades ou tabelas em um
banco de dados relacional encapsulando a criação e execução dos comandos
SQL's, sem a necessidade de se construir cada query manualmente em
determinados casos, pois o ORM permite a interação entre objetos e o
banco de dados de uma forma mais natural, semelhante a como faríamos em
qualquer linguagem de programação orientado a objetos.

Exemplos de ORM's: Hibernate (Java), Entity Framework (.NET), Django
(Python), Prisma (TypeScript/JavaScript)

![Object Relational Mapping](asserts/media/image1.png)

## **JPA (**Jakarta Persistence API**)**

O Jakarta Persistence API (JPA) é uma especificação do Java que define
como as aplicações em Java podem obter persistência e mapeamento
objeto/relacional (ORM) de dados em base de dados relacionais.
Para isso, conta com o auxílio de anotações para simplificar todo o processo.
No entanto, o JPA em si é apenas uma especificação e precisa de uma
implementação ORM para ser utilizada. No caso do Java, uma das
implementações mais comuns é o Hibernate.

**Exemplos de implementações JPA + Java**: Hibernate, EclipseLink, Apache OpenJPA

## **HIBERNATE**

O Hibernate é uma implementação ORM que deve cumprir com todos os
requisitos estabelecidos pela especificação JPA. Por ter essa
característica de ORM, ele lida com todo o mapeamento objeto/relacional
e não precisamos escrever SQL nativas (mas podemos), já que utilizamos
as anotações/especificações JPA em nosso próprio código. A linguagem
utilizada pelo Hibernate é chamada de HQL (Hibernate Query Language).

## **JDBC (**Java Database Connectivity**)**

JDBC é uma API que faz parte do Java Standard Edition e sua função
principal é permitir a conexão a sistemas de banco de dados e execução
de consultas através da linguagem SQL. Na prática, é uma camada
intermediária entre a aplicação e o banco de dados. JDBC é essencial
para interagir com bases de dados em aplicações Java, pois disponibiliza
uma interface padrão para se comunicar com sistemas de gerenciamento de
bancos de dados, facilitando a criação de aplicações que necessitam de
acesso a dados armazenados em bases de dados relacionais.

![](asserts/media/image2.png)
A arquitetura básica do JDBC é composta por duas camadas específicas:

**API JDBC**: é formada por um conjunto de classes e interfaces disponíveis nos pacotes `java.sql` e `javax.sql`, e é
responsável pela comunicação entre a aplicação e o driver JDBC.

**API JDBC Driver**: é um software que implementa os métodos necessários para realizar a comunicação com o banco de
dados.

Existem quatro tipos de drivers JDBC. São eles:

- **Driver de ponte JDBC-ODBC**: utiliza um driver ODBC para se conectar ao banco de dados.
- **Driver de API nativa**: usa bibliotecas do lado do cliente para se comunicar com o banco de dados.
- **Driver de protocolo de rede**: adota uma camada adicional de software para converter as chamadas JDBC no protocolo
  utilizado pelo banco de dados.
- **Driver fino ou Thin Driver**: converte as chamadas JDBC de acordo com o banco de dados do fabricante.

![](asserts/media/image3.png)

## **SQL (**Structured Query Language**)**

A Linguagem de consulta estruturada (SQL) é uma linguagem de programação
para armazenar e processar informações em um banco de dados relacional.
Um banco de dados relacional armazena informações em formato tabular,
com linhas e colunas representando diferentes atributos de dados e as
várias relações entre os valores dos dados. Você pode usar instruções
SQL para armazenar, atualizar, remover, pesquisar e recuperar
informações do banco de dados. Também pode usar SQL para manter e
otimizar a performance do banco de dados.

## **DBMS (**Database Management System**) \| SGBD** (Sistema de
Gerenciamento de Banco de Dados)

Um Database Management System (DBMS), ou Sistema de Gerenciamento de
Banco de Dados (SGBD), é um software que permite aos usuários criar,
manipular e gerenciar bancos de dados. Ele fornece uma interface entre
os usuários e os dados armazenados, permitindo que eles acessem,
atualizem e recuperem informações de forma eficiente.

## Exemplos de SGBDs/DBMSs Populares:

- **Relacionais**:
    - **MySQL** 🐬: Um SGBD relacional de código aberto amplamente utilizado.
    - **PostgreSQL** 🐘: Um SGBD relacional avançado e de código aberto.
    - **Oracle Database** 🏛️: Um SGBD comercial altamente robusto e escalável.
    - **Microsoft SQL Server** 🖥️: Um SGBD relacional desenvolvido pela Microsoft.

- **Não-Relacionais (NoSQL)**:
    - **MongoDB** 🍃: Um banco de dados NoSQL orientado a documentos.
    - **Cassandra** 🟪: Um banco de dados NoSQL distribuído e escalável.
    - **Redis** 🔑: Um banco de dados NoSQL de chave-valor de alto desempenho.

O pgAdmin, mysql workbeanch, dbbeaver, entre outros: são ferramentas
gráficas de gerenciamento de banco de dados. (DBMS GUI).

![](asserts/media/image4.png)

JPA

![](asserts/media/image5.png)

## **CONTEXTO DE PERSISTÊNCIA**
O Contexto de Persistência é o local
onde um conjunto de instâncias das entidades (**\@Entity**) será
gerenciado. Em uma aplicação pode haver diversos contextos, sendo que
cada contexto é gerenciado por um **_EntityManager._**
O EntityManager monitora todas as alterações realizadas nos objetos
gerenciados. Entretanto, quando um contexto é fechado, todas as
instâncias das entidades passam a não mais serem gerenciadas
(desacopladas) e qualquer alteração realizada nesta entidade não é
refletida no banco.

![](asserts/media/image6.png)

Ciclo de vida.

![](asserts/media/image7.png)

O contexto de persistência pode possuir dois tipos de escopos: transação e estendido. O escopo vai definir o ciclo de
vida de cada entidade.

### Escopo de Transação

- **Ciclo de Vida das Entidades**: Dura apenas o tempo de uma transação.
- **Após a Transação**: As instâncias gerenciadas pelo contexto de persistência são destruídas.
- **Uso**: Comum em contextos gerenciados por servidores de aplicação.
- **Exemplo Geral**: Ao fazer uma operação de banco de dados, as entidades são criadas e manipuladas dentro da
  transação. Quando a transação termina (commit ou rollback), essas entidades deixam de existir no contexto de
  persistência.
- **Exemplos Reais**:
    - **Operações Bancárias**: Um sistema bancário processando uma transferência de dinheiro. A transferência envolve
      verificar o saldo, debitar uma conta e creditar outra, tudo dentro de uma única transação. Após a transação, as
      entidades usadas para essa operação são descartadas.
    - **E-commerce: Processamento de Pedido**: Um cliente realiza uma compra. O sistema verifica a disponibilidade dos
      produtos, debita o pagamento e atualiza o estoque, tudo dentro de uma única transação. Isso garante a consistência
      dos dados durante a operação de checkout.

### Escopo Estendido

- **Ciclo de Vida das Entidades**: Dura além de uma transação, persistindo mesmo após a transação.
- **Após a Transação**: As instâncias continuam sendo gerenciadas pelo contexto de persistência.
- **Uso**: Utilizado em ambientes JSE e em ambientes JEE, sendo gerenciado por componentes com escopo de sessão.
- **Exemplo Geral**: Ideal para cenários onde a mesma instância de entidade precisa ser usada em múltiplas transações,
  como em aplicações de longo prazo ou processos de conversação.
- **Exemplos Reais**:
    - **Aplicações de Longo Prazo**: Sistema de gerenciamento de projetos onde um usuário trabalha em um projeto por
      vários dias. As mudanças feitas no projeto são salvas periodicamente, mas o projeto em si é mantido no contexto de
      persistência durante toda a sessão do usuário.
    - **Processos de Conversação (Wizard)**: Formulário de inscrição dividido em várias etapas (wizard). Um usuário
      preenche várias seções do formulário em diferentes momentos. As informações preenchidas em cada etapa são mantidas
      no contexto de persistência até que o formulário completo seja submetido.
    - **Aplicações de Edição de Dados**: Sistema de edição de documentos colaborativos. Vários usuários podem editar um
      documento ao longo do tempo. As mudanças de cada usuário são persistidas, mas o documento continua a ser
      gerenciado pelo contexto de persistência.

### Exemplo de Implementação

```java
//JEE
@PersistenceContext(unitName = "exemploPU", type = PersistenceContextType.EXTENDED)
private EntityManager entityManager;

//Spring boot
@PersistenceContext(type = PersistenceContextType.EXTENDED)
private EntityManager entityManager;
```

**Explicação:** Este código configura um EntityManager com um escopo de
persistência estendido, indicando que as entidades gerenciadas por este
EntityManager continuarão a ser gerenciadas mesmo após o fim de uma
transação.

### Resumindo

- **Escopo de Transação**: Ciclo de vida limitado à transação.

- **Escopo Estendido**: Ciclo de vida além da transação, adequado para uso prolongado

## EntityManager

O EntityManager é um serviço responsável por gerenciar as entidades.

![](asserts/media/image8.png)

## Estados de uma Entidade em JPA/Hibernate

Uma entidade (@Entity) pode estar em um destes quatro estados:

1. **NEW**: Quando temos uma nova instância da entidade que não está associada ao contexto de persistência. Esse é o
   estado inicial de uma entidade recém-criada, antes de ser persistida pela primeira vez.

2. **MANAGED**: É uma instância persistente que está atualmente associada ao contexto de persistência. O EntityManager
   está gerenciando essa entidade, o que significa que qualquer alteração feita à entidade será automaticamente
   sincronizada com o banco de dados quando a transação for confirmada.

3. **REMOVED**: É uma instância associada ao contexto de persistência que será removida do banco de dados quando a
   transação for confirmada. Isso acontece quando o método `remove()` do EntityManager é chamado.

4. **DETACHED**: É uma instância que estava no contexto de persistência, mas não está mais associada ao mesmo. Isso pode
   ocorrer quando a sessão é fechada, a transação é concluída, ou quando explicitamente desanexada usando o método
   `detach()` do EntityManager. Uma entidade nesse estado não é monitorada pelo EntityManager, portanto, mudanças feitas
   à entidade não serão sincronizadas com o banco de dados.

O EntityManager é uma interface da JPA que define os métodos para gerenciar as entidades.

#### Principais Operações

- **persist()**: Faz a transição do estado NEW para MANAGED. A entidade é salva no banco de dados.
- **merge()**: Faz a transição do estado DETACHED para MANAGED. A entidade é sincronizada com o banco de dados.
- **remove()**: Faz a transição do estado MANAGED para REMOVED. A entidade é removida do banco de dados.
- **find()**: Busca uma entidade pelo seu identificador. Se encontrada, a entidade será colocada no estado MANAGED.
- **createQuery()**: Cria uma query para ser executada no banco de dados.

#### Ciclo de Vida da Entidade

1. Instância da entidade é criada (NEW).
2. A entidade é persistida pelo EntityManager (MANAGED).
3. A transação é confirmada e a entidade é sincronizada com o banco de dados.
4. A entidade pode ser removida (REMOVED) ou destacada (DETACHED).

![](asserts/media/image9.png)

### Exemplos de Uso

- **NEW**:

  ```java
  EntityManager em = entityManagerFactory.createEntityManager();

  em.getTransaction().begin();

  MyEntity entity = new MyEntity(); // Estado NEW

  em.persist(entity); // Estado passa a ser MANAGED

  em.getTransaction().commit();

  em.close();
  ```

- **MANAGED**:

  ```java


  EntityManager em = entityManagerFactory.createEntityManager();

  em.getTransaction().begin();

  MyEntity entity = em.find(MyEntity.class, entityId); // Estado
  MANAGED

  entity.setSomeProperty(\"newValue\");

  em.getTransaction().commit(); // As mudanças são sincronizadas com o
  banco de dados

  em.close();
  ```

- **REMOVED**:

  ```java


  EntityManager em = entityManagerFactory.createEntityManager();

  em.getTransaction().begin();

  MyEntity entity = em.find(MyEntity.class, entityId); // Estado
  MANAGED

  em.remove(entity); // Estado REMOVED

  em.getTransaction().commit(); // A entidade é removida do banco de
  dados

  em.close();
  ```

- **DETACHED**:

  ```java

  Copiar código

  EntityManager em = entityManagerFactory.createEntityManager();

  em.getTransaction().begin();

  MyEntity entity = em.find(MyEntity.class, entityId); // Estado
  MANAGED

  em.detach(entity); // Estado DETACHED

  entity.setSomeProperty(\"newValue\");

  em.getTransaction().commit(); // Mudança não sincronizada com o
  banco de dados

  em.close();
  ```

### Complementos

- **Flushing**: O estado MANAGED garante que qualquer modificação
  feita à entidade será sincronizada com o banco de dados durante o
  processo de flush. O flush pode ser manual (usando em.flush()) ou
  automático ao final de uma transação.

- **Merge**: Uma entidade no estado DETACHED pode ser mesclada de
  volta ao contexto de persistência usando o método merge(). Isso
  cria uma cópia da entidade no estado MANAGED.

  ```java

  MyEntity detachedEntity = \...;

  EntityManager em = entityManagerFactory.createEntityManager();

  em.getTransaction().begin();

  MyEntity managedEntity = em.merge(detachedEntity); // Estado MANAGED

  em.getTransaction().commit();

  em.close();

  ```

- **Cascade Types**: Para entidades relacionadas, é importante
  entender os tipos de cascata (Cascading) que podem ser aplicados
  nas associações, como CascadeType.PERSIST, CascadeType.MERGE,
  CascadeType.REMOVE, CascadeType.DETACH, entre outros. Isso define
  como as operações de persistência devem ser propagadas para
  entidades associadas.

Referências:

<https://coodesh.com/blog/dicionario/o-que-e-hibernate/>

<https://www.dio.me/articles/entenda-a-diferenca-entre-jdbc-jpa-hibernate-e-o-spring-data-jpa>

<https://rockcontent.com/br/blog/jdbc/>

<https://aws.amazon.com/pt/what-is/sql/>

<https://w2websites.com/glossario/o-que-e-database-management-system-dbms/>

<https://www.youtube.com/watch?v=xGnzDzcXSnA&list=PLlYjHWCxjWmBptKGdbbRfIhj0XJttbs6B>

<https://ricardojob.gitbooks.io/dac/content/contexto.html>

---

# Projeto de Estudo 2: Hibernate com OpenBootcamp

Este projeto foi desenvolvido como parte do curso de **Hibernate** ministrado pelo canal **OpenBootcamp** no YouTube. A
trilha do curso abordou diversos conceitos fundamentais e avançados do **Hibernate** e sua integração com o **Spring
Boot**, além de técnicas para otimização e manipulação de dados em aplicações Java. Confira a playlist completa do
curso [neste link](https://www.youtube.com/playlist?list=PLkVpKYNT_U9eTicIIaqR9YUoIZ5V4ZXpS).

## 📚 Tópicos Estudados

### 1. Introdução ao Hibernate

- Visão geral sobre o que é o **Hibernate** e como ele facilita o **mapeamento objeto-relacional** (ORM).
- Configuração básica de um projeto com **Hibernate** e integração com **Spring Boot**.

### 2. Criação de Modelos e Entidades

- Como definir **entidades** em Hibernate, mapeando classes Java para tabelas de um banco de dados.
- **Anotações** como `@Entity`, `@Table`, `@Id` e `@GeneratedValue` para mapeamento.

### 3. Associações e Relacionamentos

- **Relacionamentos entre entidades**:
    - **OneToOne** (`@OneToOne`)
    - **OneToMany** (`@OneToMany`)
    - **ManyToOne** (`@ManyToOne`)
    - **ManyToMany** (`@ManyToMany`)
- Estudo de **cascading**, **fetch types** e como gerenciar esses relacionamentos no banco de dados.

### 4. Coleções em Hibernate

- Uso de coleções (`List`, `Set`, etc.) em entidades e como o **Hibernate** gerencia essas coleções de forma eficiente.

### 5. Consultas com HQL e Criteria API

- Utilização de **Hibernate Query Language (HQL)** para criar consultas mais flexíveis em bancos de dados.
- Introdução à **Criteria API** para a construção dinâmica de consultas.

### 6. Eventos no Ciclo de Vida das Entidades

- Como usar **eventos de ciclo de vida** no Hibernate, como:
    - `@PrePersist`
    - `@PreUpdate`
    - `@PreRemove`

### 7. Auditoria de Dados com Envers

- Implementação de auditoria de dados utilizando **Hibernate Envers**.
- Rastreabilidade das alterações feitas em entidades ao longo do tempo.

### 8. Paginação e Gerenciamento de Erros

- Implementação de **paginação** em consultas para melhorar a performance e experiência de usuário.
- Tratamento de **transações** e **erros** na aplicação utilizando o **Spring Boot**.

### 9. Armazenamento de Dados em JSON

- Como armazenar e recuperar dados em formato **JSON** em entidades utilizando o Hibernate e o PostgreSQL.

### 10. Inserções em Lote (Batch Insert)

- Otimização de inserções em massa no banco de dados, utilizando o suporte do Hibernate para **batch processing**.

### 11. Integração com PostgreSQL

- Configuração do **PostgreSQL** como banco de dados para persistência de dados.
- Uso de **replicação de bancos de dados** e **criação de clusters** para aumentar a escalabilidade da aplicação.

### 12. Backups e Segurança no PostgreSQL

- Como realizar **backups** e restaurar bancos de dados em ambientes PostgreSQL.
- Gerenciamento de **níveis de segurança** e permissões no PostgreSQL.

### 13. Otimização de Consultas e Aplicações

- Técnicas de **otimização** de consultas e manipulação de dados utilizando índices e boas práticas de banco de dados.

