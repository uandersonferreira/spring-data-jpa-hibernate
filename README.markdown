**JPA / HIBERNATE INTRODUÇÃO**

**ORM** (Object Relational Mapping)

Mapeamento objeto-relacional ou ORM é a técnica de programação para
mapear objetos do modelo de domínio do aplicativo (class Java) para as
tabelas do banco de dados relacional. O conceito fundamental por trás do
ORM é como os objetos se relacionam entre si e como se representa essas
relações como entidades em bancos de dados. De forma resumida, ele atua
como uma ponte/intermediador, que fornece uma camada que relaciona os
dados de um modelo baseado em objeto com entidades ou tabelas em um
banco de dados relacional encapsulando a criação e execução dos comandos
SQL's, sem a necessidade de se construir cada query manualmente em
determinados casos, pois o ORM permite a interação entre objetos e o
banco de dados de uma forma mais natural, semelhante a como faríamos em
qualquer linguagem de programação orientado a objetos.

Exemplos de ORM's: Hibernate (Java), Entity Framework (.NET), Django
(Python), Prisma (TypeScript/JavaScript)

![Object Relational Mapping](asserts/media/image1.png)

**JPA (**Jakarta Persistence API**)**

O Jakarta Persistence API (JPA)é uma especificação do Java que define
como as aplicações em java podem obter persistência e mapeamento
objeto/relacional (ORM) de dados em base de dados relacionais, para isso
conta com o auxílio de anotações, para simplificar todo o processo. No
entanto, o JPA em sí é apenas uma especificação e precisa de uma
implementação ORM para ser utilizada, no caso do Java é o Hibernate.

Exemplos de implementações JPA + Java: Hibernate, EclipseLink , Apache
OpenJPA

**HIBERNATE**

O Hibernate é uma implementação ORM que deve cumprir com todos os
requisitos estabelecidos pela especificação JPA.

Por ter essa característica de ORM, ele lida com todo o mapeamento
objeto/relacional e não precisamos escrever SQL nativas (mas podemos),
já que utilizamos as anotações/especificações (jpa) em nosso próprio
código, que é chamado de HQL, ou seja, Hibernate Query Language, para
que tudo funcione.

**JDBC (**Java Database Connectivity**)**

É uma API que faz parte do Java Standard Edition e sua função principal
é permitir a conexão a sistemas de banco de dados e execução de
consultas através da linguagem sql, na prática é uma camada
intermediária entre a aplicação e o banco de dados.

JDBC é essencial para interagir com bases de dados em aplicações Java,
pois disponibiliza uma interface padrão para se comunicar com sistemas
de gerenciamento de bancos de dados, facilitando a criação de aplicações
que necessitam de acesso a dados armazenados em bases de dados
relacionais.

![](asserts/media/image2.png)

A arquitetura básica do JDBC é composta por duas camadas específicas:

**API JDBC:** que é formada por um conjunto de classes e interfaces
disponíveis nos pacotes java.sql e javax.sql, e é responsável pela
comunicação entre a aplicação e o driver JDBC.

**API JDBC Driver:** é um software que implementa os métodos necessários
para realizar a comunicação com o banco de dados.

Existem quatro tipos de drivers JDBC. São eles:

\- driver de ponte JDBC-ODBC: utiliza um driver ODBC para se conectar ao
banco de dados;

\- driver de API nativa: usa bibliotecas do lado do cliente para se
comunicar com o banco de dados;

\- driver de protocolo de rede: adota uma camada adicional de software
para converter as chamadas JDBC no protocolo utilizado pelo banco de
dados;

\- driver fino ou Thin Driver: converte as chamadas JDBC de acordo com o
banco de dados do fabricante

![](asserts/media/image3.png)

**SQL (**Structured Query Language**)**

A Linguagem de consulta estruturada (SQL) é uma linguagem de programação
para armazenar e processar informações em um banco de dados relacional.
Um banco de dados relacional armazena informações em formato tabular,
com linhas e colunas representando diferentes atributos de dados e as
várias relações entre os valores dos dados. Você pode usar instruções
SQL para armazenar, atualizar, remover, pesquisar e recuperar
informações do banco de dados. Também pode usar SQL para manter e
otimizar a performance do banco de dados.

**DBMS (**Database Management System**) \| SGBD** (Sistema de
Gerenciamento de Banco de Dados)

Um Database Management System (DBMS), ou Sistema de Gerenciamento de
Banco de Dados (SGBD), é um software que permite aos usuários criar,
manipular e gerenciar bancos de dados. Ele fornece uma interface entre
os usuários e os dados armazenados, permitindo que eles acessem,
atualizem e recuperem informações de forma eficiente.

### Exemplos de SGBDs/DBMSs Populares:

- **Relacionais**:

    - **MySQL**: Um SGBD relacional de código aberto amplamente

      > utilizado.

    - **PostgreSQL**: Um SGBD relacional avançado e de código aberto.

    - **Oracle Database**: Um SGBD comercial altamente robusto e

      > escalável.

    - **Microsoft SQL Server**: Um SGBD relacional desenvolvido pela
      > Microsoft.

- **Não-Relacionais (NoSQL)**:

    - **MongoDB**: Um banco de dados NoSQL orientado a documentos.

    - **Cassandra**: Um banco de dados NoSQL distribuído e escalável.

    - **Redis**: Um banco de dados NoSQL de chave-valor de alto
      > desempenho.

O pgAdmin, mysql workbeanch, dbbeaver, entre outros: são ferramentas
gráficas de gerenciamento de banco de dados. (DBMS GUI).

![](asserts/media/image4.png)

JPA

![](asserts/media/image5.png)

**CONTEXTO DE PERSISTÊNCIA** -- O Contexto de Persistência é o local
onde um conjunto de instâncias das entidades (**\@Entity**) será
gerenciado. Em uma aplicação pode haver diversos contextos, sendo que
cada contexto é gerenciado por um**_EntityManager._**
O EntityManager monitora todas as alterações realizadas nos objetos
gerenciados. Entretanto, quando um contexto é fechado, todas as
instâncias das entidades passam a não mais serem gerenciadas
(desacopladas) e qualquer alteração realizada nesta entidade não é
refletida no banco.

![](asserts/media/image6.png)

Ciclo de vida.

![](asserts/media/image7.png)

O contexto de persistência pode possuir dois tipos de
escopos:transação e estendido. O escopo vai definir o ciclo de vida de
cada entidade.

### Escopo de Transação

- **Ciclo de Vida das Entidades**: Dura apenas o tempo de uma

  > transação.

- **Após a Transação**: As instâncias gerenciadas pelo contexto de

  > persistência são destruídas.

- **Uso**: Comum em contextos gerenciados por servidores de aplicação.

- **Exemplo Geral**: Ao fazer uma operação de banco de dados, as

  > entidades são criadas e manipuladas dentro da transação. Quando a
  > transação termina (commit ou rollback), essas entidades deixam de
  > existir no contexto de persistência.

- **Exemplos Reais**:

    - **Operações Bancárias**: Um sistema bancário processando uma

      > transferência de dinheiro. A transferência envolve verificar o
      > saldo, debitar uma conta e creditar outra, tudo dentro de uma
      > única transação. Após a transação, as entidades usadas para
      > essa operação são descartadas.

    - **E-commerce: Processamento de Pedido**: Um cliente realiza uma
      > compra. O sistema verifica a disponibilidade dos produtos,
      > debita o pagamento e atualiza o estoque, tudo dentro de uma
      > única transação. Isso garante a consistência dos dados durante
      > a operação de checkout.

### Escopo Estendido

- **Ciclo de Vida das Entidades**: Dura além de uma transação,

  > persistindo mesmo após a transação.

- **Após a Transação**: As instâncias continuam sendo gerenciadas pelo

  > contexto de persistência.

- **Uso**: Utilizado em ambientes JSE e em ambientes JEE, sendo

  > gerenciado por componentes com escopo de sessão.

- **Exemplo Geral**: Ideal para cenários onde a mesma instância de

  > entidade precisa ser usada em múltiplas transações, como em
  > aplicações de longo prazo ou processos de conversação.

- **Exemplos Reais**:

    - **Aplicações de Longo Prazo**: Sistema de gerenciamento de

      > projetos onde um usuário trabalha em um projeto por vários
      > dias. As mudanças feitas no projeto são salvas periodicamente,
      > mas o projeto em si é mantido no contexto de persistência
      > durante toda a sessão do usuário.

    - **Processos de Conversação (Wizard)**: Formulário de inscrição

      > dividido em várias etapas (wizard). Um usuário preenche várias
      > seções do formulário em diferentes momentos. As informações
      > preenchidas em cada etapa são mantidas no contexto de
      > persistência até que o formulário completo seja submetido.

    - **Aplicações de Edição de Dados**: Sistema de edição de
      > documentos colaborativos. Vários usuários podem editar um
      > documento ao longo do tempo. As mudanças de cada usuário são
      > persistidas, mas o documento continua a ser gerenciado pelo
      > contexto de persistência.

### Exemplo de Implementação no Ambiente JEE

java

Copiar código

\@PersistenceContext(unitName = \"exemploPU\", type =
PersistenceContextType.EXTENDED)

EntityManager entityManager;

- **Explicação**: Este código configura um EntityManager com um escopo
  > de persistência estendido, indicando que as entidades gerenciadas
  > por este EntityManager continuarão a ser gerenciadas mesmo após o
  > fim de uma transação.

### Resumindo

- **Escopo de Transação**: Ciclo de vida limitado à transação.

- **Escopo Estendido**: Ciclo de vida além da transação, adequado para
  > uso prolongado

## EntityManager

O EntityManager é um serviço responsável por gerenciar as entidades.

![](asserts/media/image8.png)

### Estados de uma Entidade em JPA/Hibernate

Uma entidade (@Entity) pode estar em um destes quatro estados:

1. **NEW**: Quando temos uma nova instância da entidade que não está

   > associada ao contexto de persistência. Esse é o estado inicial de
   > uma entidade recém-criada, antes de ser persistida pela primeira
   > vez.

2. **MANAGED**: É uma instância persistente que está atualmente

   > associada ao contexto de persistência. O EntityManager está
   > gerenciando essa entidade, o que significa que qualquer alteração
   > feita à entidade será automaticamente sincronizada com o banco de
   > dados quando a transação for confirmada.

3. **REMOVED**: É uma instância associada ao contexto de persistência

   > que será removida do banco de dados quando a transação for
   > confirmada. Isso acontece quando o método remove() do
   > EntityManager é chamado.

4. **DETACHED**: É uma instância que estava no contexto de

   > persistência, mas não está mais associada ao mesmo. Isso pode
   > ocorrer quando a sessão é fechada, a transação é concluída, ou
   > quando explicitamente desanexada usando o método detach() do
   > EntityManager. Uma entidade nesse estado não é monitorada pelo
   > EntityManager, portanto, mudanças feitas à entidade não serão
   > sincronizadas com o banco de dados.

   O EntityManager é uma interface da JPA que define os métodos para
   gerenciar as entidades.

#### Principais Operações {#principais-operações .list-paragraph}

- persist(): Faz a transição do estado NEW para MANAGED. A entidade é

  > salva no banco de dados.

- merge(): Faz a transição do estado DETACHED para MANAGED. A entidade

  > é sincronizada com o banco de dados.

- remove(): Faz a transição do estado MANAGED para REMOVED. A entidade

  > é removida do banco de dados.

- find(): Busca uma entidade pelo seu identificador. Se encontrada, a

  > entidade será colocada no estado MANAGED.

- createQuery(): Cria uma query para ser executada no banco de dados.

#### Ciclo de Vida da Entidade {#ciclo-de-vida-da-entidade .list-paragraph}

1. Instância da entidade é criada (NEW).

2. A entidade é persistida pelo EntityManager (MANAGED).

3. A transação é confirmada e a entidade é sincronizada com o banco de

   > dados.

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

  > feita à entidade será sincronizada com o banco de dados durante o
  > processo de flush. O flush pode ser manual (usando em.flush()) ou
  > automático ao final de uma transação.

- **Merge**: Uma entidade no estado DETACHED pode ser mesclada de

  > volta ao contexto de persistência usando o método merge(). Isso
  > cria uma cópia da entidade no estado MANAGED.

  ```java

  MyEntity detachedEntity = \...;

  EntityManager em = entityManagerFactory.createEntityManager();

  em.getTransaction().begin();

  MyEntity managedEntity = em.merge(detachedEntity); // Estado MANAGED

  em.getTransaction().commit();

  em.close();

  ```

- **Cascade Types**: Para entidades relacionadas, é importante
  > entender os tipos de cascata (Cascading) que podem ser aplicados
  > nas associações, como CascadeType.PERSIST, CascadeType.MERGE,
  > CascadeType.REMOVE, CascadeType.DETACH, entre outros. Isso define
  > como as operações de persistência devem ser propagadas para
  > entidades associadas.

Referências:

<https://coodesh.com/blog/dicionario/o-que-e-hibernate/>

<https://www.dio.me/articles/entenda-a-diferenca-entre-jdbc-jpa-hibernate-e-o-spring-data-jpa>

<https://rockcontent.com/br/blog/jdbc/>

<https://aws.amazon.com/pt/what-is/sql/>

<https://w2websites.com/glossario/o-que-e-database-management-system-dbms/>

<https://www.youtube.com/watch?v=xGnzDzcXSnA&list=PLlYjHWCxjWmBptKGdbbRfIhj0XJttbs6B>

<https://ricardojob.gitbooks.io/dac/content/contexto.html>
