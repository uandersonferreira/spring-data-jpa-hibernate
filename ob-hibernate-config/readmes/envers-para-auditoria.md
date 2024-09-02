Aqui está uma revisão do conteúdo sobre o Envers no Hibernate, junto com um exemplo simples de uso da anotação
`@Audited` tanto a nível de classe quanto de atributo, considerando também as relações.

### Envers no Hibernate 6

O Envers é uma ferramenta integrada ao Hibernate que permite auditar mudanças realizadas em entidades de maneira
automática. Para utilizá-lo, basta adicionar o jar `hibernate-envers` no classpath do projeto e anotar as entidades com
`@Audited`. A partir daí, o Envers criará automaticamente tabelas de auditoria que armazenam o histórico das mudanças
sempre que uma transação é confirmada.

#### Configuração

Não é necessário configurar listeners manualmente no arquivo de configuração do Hibernate, como era feito em versões
anteriores. O Envers registra os listeners automaticamente ao detectar o jar no classpath.

#### Tabelas de Auditoria

Para cada entidade anotada com `@Audited`, o Envers cria uma tabela adicional com o sufixo `_AUD`, onde são armazenadas
as versões históricas da entidade. Também é criada a tabela `REVINFO`, que guarda informações sobre cada revisão.

### Exemplo de Uso do Envers

#### 1. Auditoria de uma Entidade Simples

Aqui está um exemplo básico de como usar a anotação `@Audited` em uma entidade simples:

```java
import org.hibernate.envers.Audited;
import jakarta.persistence.*;

@Audited
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    // Getters e setters omitidos para brevidade
}
```

Nesse exemplo, a entidade `Customer` será totalmente auditada, e todas as mudanças realizadas em qualquer um dos seus
atributos serão registradas na tabela `Customer_AUD`.

#### 2. Auditoria de Atributos Específicos

Se quiser auditar apenas alguns atributos da entidade, você pode anotar apenas esses atributos com `@Audited`:

```java
import org.hibernate.envers.Audited;
import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    private String name;

    private Double price;

    @Audited
    private String category;

    // Getters e setters omitidos para brevidade
}
```

Neste exemplo, apenas os atributos `name` e `category` da entidade `Product` serão auditados. As mudanças no atributo
`price` não serão registradas.

#### 3. Auditoria em Relações

O Envers também suporta auditoria de relações entre entidades, como `@OneToMany`, `@ManyToOne`, etc. Para auditar uma
relação, basta anotar a propriedade de relacionamento com `@Audited`:

```java
import org.hibernate.envers.Audited;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Audited
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order")
    @Audited
    private List<OrderItem> items;

    // Getters e setters omitidos para brevidade
}
```

Neste exemplo, a entidade `Order` e a lista de itens (`items`) serão auditadas. Se houver mudanças nos itens associados
a uma `Order`, essas mudanças também serão registradas na tabela de auditoria.

### 4. Acesso ao histórico de mudanças

O Envers permite acessar o histórico de mudanças de uma entidade utilizando a interface AuditReader, que é obtida
através do AuditReaderFactory. Com isso, é possível recuperar revisões específicas e inspecionar o estado da entidade em
um determinado ponto no tempo.

#### Cenário

Vamos considerar a entidade `Customer`, que está sendo auditada com o Envers. Queremos recuperar o histórico de revisões dessa entidade e inspecionar o estado dela em diferentes pontos no tempo.

### Código de Exemplo

1. **Configuração da Entidade `Customer`**:

   ```java
   import org.hibernate.envers.Audited;
   import jakarta.persistence.*;

   @Audited
   @Entity
   public class Customer {

       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;

       private String firstName;
       
       private String lastName;

       @Temporal(TemporalType.TIMESTAMP)
       private Date createdOn;

       // Getters e setters omitidos para brevidade
   }
   ```

2. **Recuperando o Histórico de Revisões**:

   O exemplo abaixo mostra como utilizar o `AuditReader` para acessar as revisões de uma entidade `Customer`:

   ```java
   import org.hibernate.envers.AuditReader;
   import org.hibernate.envers.AuditReaderFactory;
   import jakarta.persistence.EntityManager;
   import jakarta.persistence.EntityManagerFactory;
   import jakarta.persistence.Persistence;
   import java.util.List;

   public class CustomerAuditExample {

       public static void main(String[] args) {
           EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
           EntityManager em = emf.createEntityManager();

           try {
               // Recupera o AuditReader a partir do EntityManager
               AuditReader auditReader = AuditReaderFactory.get(em);

               // ID da entidade que queremos verificar
               Long customerId = 1L;

               // Obtém a lista de números de revisão para a entidade Customer com o ID fornecido
               List<Number> revisions = auditReader.getRevisions(Customer.class, customerId);

               for (Number revision : revisions) {
                   // Para cada revisão, recupera o estado da entidade nesse ponto no tempo
                   Customer customerAtRevision = auditReader.find(Customer.class, customerId, revision);
                   System.out.println("Revisão: " + revision);
                   System.out.println("Nome: " + customerAtRevision.getFirstName());
                   System.out.println("Sobrenome: " + customerAtRevision.getLastName());
                   System.out.println("Criado em: " + customerAtRevision.getCreatedOn());
                   System.out.println("----");
               }
           } finally {
               em.close();
               emf.close();
           }
       }
   }
   ```

### Explicação do Código

1. **AuditReaderFactory**:
   O `AuditReaderFactory` é usado para obter uma instância de `AuditReader`, que é o principal ponto de entrada para consultar o histórico de revisões de uma entidade.

2. **AuditReader.getRevisions**:
   Esse método retorna uma lista de números de revisão para a entidade `Customer` com o ID fornecido. Cada número de revisão representa um ponto no tempo em que a entidade foi alterada.

3. **AuditReader.find**:
   Esse método recupera o estado da entidade `Customer` em um ponto específico no tempo, identificado pelo número de revisão. É como se você estivesse "voltando no tempo" para ver como a entidade estava naquela revisão.

4. **Impressão das Revisões**:
   No exemplo, para cada revisão, o estado da entidade `Customer` é impresso no console, mostrando o nome, sobrenome e a data de criação.


### Conclusão

Em resumo, o Envers no Hibernate 6 facilita a implementação de auditoria em aplicações Java, oferecendo uma forma
simples e eficiente de rastrear mudanças em entidades ao longo do tempo, com mínima configuração necessária.