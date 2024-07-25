### JPA e Hibernate: Definições de Entidades em Java

**Construtores:**

- **JPA:** A especificação Jakarta Persistence define que uma entidade deve ter um construtor sem argumentos que seja
  público ou protegido.

  ```java
  public class Aluno {
      public Aluno() {
      }
  }
  ```

- **Hibernate:** Além de aceitar construtores públicos e protegidos, permite construtores com visibilidade no nível do
  pacote.

  ```java
  public class Aluno {
      Aluno() {
      }
  }
  ```

**Classes de Alto Nível:**

- **JPA:** Exige que a entidade seja uma classe de alto nível (não pode ser aninhada).
- **Hibernate:** Não impõe essa restrição, mas deve ser statica (static)

Uma **classe de alto nível** em Java, no contexto de JPA, é uma classe que não é aninhada (nested) dentro de outra
classe. Em outras palavras, uma classe de alto nível é uma classe que é definida diretamente dentro de um arquivo de
origem (source file) e não está contida dentro de outra classe.

### Exemplos:

#### Classe de Alto Nível (High-level class):

```java

@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    // Construtor sem argumentos
    public Aluno() {
    }

    // Getters e setters
}
```

Neste exemplo, a classe `Aluno` é uma classe de alto nível porque não está definida dentro de outra classe.

#### Classe Aninhada (Nested class) - $ representa classes aninhadas:
ex: Escola$Aluno

```java
public class Escola {

    @Entity
    public static class Aluno {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nome;

        // Construtor sem argumentos
        public Aluno() {
        }

        // Getters e setters
    }
}
```

Neste exemplo, a classe `Aluno` é uma classe aninhada porque está definida dentro da classe `Escola`. De acordo com as
especificações do JPA, uma entidade não pode ser uma classe aninhada; ela deve ser uma classe de alto nível.

### Requisitos do JPA:

- **JPA:** Exige que as entidades sejam classes de alto nível. Portanto, no exemplo acima, a classe `Aluno` não seria
  válida como entidade JPA se fosse aninhada dentro da classe `Escola`.
- **Hibernate:** Não impõe essa restrição. O Hibernate permite que entidades sejam classes aninhadas, desde que a classe
  aninhada seja estática (static).

### Motivo para a Restrição do JPA:

A razão para essa restrição em JPA é principalmente para simplificar o gerenciamento de entidades e garantir uma
estrutura clara e consistente. Entidades JPA precisam ser gerenciadas pelo provedor de persistência (como o Hibernate),
e ter classes de alto nível facilita esse gerenciamento. Classes aninhadas podem introduzir complexidade adicional e
dificuldades na serialização e na reflexão, que são frequentemente usadas em frameworks de persistência.

**Campos Finais:**

- **JPA:** Entidades e seus campos persistentes não podem ser finais.
- **Hibernate:** Permite campos finais, mas isso pode interferir na geração de proxies para carregamento lento (lazy
  loading).

**Acessibilidade dos Atributos:**

- **JPA:** Acessibilidade dos atributos persistentes deve ser feita usando getters e setters.
- **Hibernate:** Não impõe restrições rigorosas sobre isso.

### Anotações Comuns:

1. **@Entity:**
    - **Uso:** Indica que a classe é uma entidade JPA.
    ```java
    @Entity
    public class Aluno {
        // campos e métodos
    }
    ```

2. **@Table:**
    - **Uso:** Define propriedades diretamente relacionadas à tabela no banco de dados.
    - **Atributos Comuns:** `name` e `schema`.
    ```java
    @Table(name = "aluno_local", schema = "tmp_skem")
    ```

3. **@Id:**
    - **Uso:** Indica o identificador da entidade (chave primária).
    ```java
    @Id
    private Long id;
    ```

4. **@GeneratedValue:**
    - **Uso:** Especifica a estratégia de geração de chave primária.
    - **Estratégias Comuns:**
        - `AUTO`: O provedor de persistência escolhe a estratégia.
        - `IDENTITY`: Usa colunas de identidade do banco de dados (e.g., `AUTO_INCREMENT`, `SERIAL`).
        - `SEQUENCE`: Usa geradores de sequência.
        - `TABLE`: Usa uma tabela para armazenar valores de chave primária.
        - `UUID`: (adição do Hibernate, não faz parte do JPA)

### Estratégias Comuns de Geração de Chave Primária:

1. **AUTO:**
    - **Conceito:** O provedor de persistência (como o Hibernate) escolhe automaticamente a estratégia de geração de
      chaves primárias com base no banco de dados subjacente e nas configurações de mapeamento.
    - **Vantagem:** Flexível e simples de usar, pois não requer configuração adicional.
    - **Desvantagem:** Pode resultar em comportamento inconsistente em diferentes ambientes de banco de dados.

    ```java
    @GeneratedValue(strategy = GenerationType.AUTO)
    ```

2. **IDENTITY:**
    - **Conceito:** Utiliza colunas de identidade do banco de dados que geram valores automaticamente para novas linhas.
      Por exemplo, em MySQL, `AUTO_INCREMENT` ou em PostgreSQL, `SERIAL`.
    - **Vantagem:** Simples de configurar e usar, com suporte nativo do banco de dados.
    - **Desvantagem:** Pode não ser a melhor escolha para bancos de dados distribuídos devido à falta de controle sobre
      a geração de IDs.

    ```java
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ```

3. **SEQUENCE:**
    - **Conceito:** Usa geradores de sequência do banco de dados para obter valores únicos. As sequências são objetos no
      banco de dados que geram um valor numérico incremental cada vez que são chamados.
    - **Vantagem:** Controle completo sobre a geração de IDs, bom desempenho, e adequado para bancos de dados
      distribuídos.
    - **Desvantagem:** Requer suporte específico do banco de dados para sequências.

    ```java
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @SequenceGenerator(name = "student_seq", sequenceName = "student_sequence", initialValue = 20, allocationSize = 1)
    ```

4. **TABLE:**
    - **Conceito:** Usa uma tabela especial no banco de dados para armazenar e gerar valores de chave primária. A tabela
      mantém uma coluna com o último valor gerado, que é incrementado para gerar novos valores.
    - **Vantagem:** Pode ser usado em qualquer banco de dados, independentemente do suporte para colunas de identidade
      ou sequências.
    - **Desvantagem:** Pode ser menos eficiente do que outras estratégias devido ao acesso adicional à tabela de geração
      de chaves.

    ```java
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
    @TableGenerator(name = "table_gen", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_val", initialValue = 0, allocationSize = 1)
    ```

5. **UUID:**
    - **Conceito:** Gera chaves primárias como UUIDs (Universally Unique Identifiers). O UUID é um valor único de 128
      bits que pode ser gerado de forma independente em diferentes sistemas sem colisão.
    - **Vantagem:** Garante unicidade global, útil para sistemas distribuídos e integração com outras aplicações.
    - **Desvantagem:** Pode ser menos eficiente em termos de armazenamento e performance devido ao tamanho maior do UUID
      em comparação com valores numéricos.

    ```java
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    ```

### Considerações Finais:

- **AUTO:** Ideal para desenvolvimento rápido e testes. Não recomendado para ambientes de produção com requisitos
  específicos de desempenho e consistência.
- **IDENTITY:** Simples e eficiente para muitos cenários, mas cuidado com bancos de dados distribuídos.
- **SEQUENCE:** Melhor escolha para bancos de dados que suportam sequências, garantindo alto desempenho e controle.
- **TABLE:** Utilizável em qualquer banco de dados, mas com potencial impacto de desempenho.
- **UUID:** Excelente para unicidade global, especialmente em sistemas distribuídos, mas com possíveis impactos em
  armazenamento e desempenho.

### Exemplo com uma PK Customizada:

```java

@Entity
public class CustomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_generator")
    @GenericGenerator(name = "custom_generator", strategy = "com.exemplo.CustomStringGenerator")
    private String id;

    private String nome;

    // Construtor sem argumentos
    public CustomEntity() {
    }

    // Outros construtores, getters e setters
}
```

```java
public class CustomStringGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return "udc-" + UUID.randomUUID().toString();
    }
}
```

### Considerações Finais:

- **JPA vs. Hibernate:** É importante estar ciente das diferenças ao usar JPA com Hibernate, especialmente em termos de
  visibilidade de construtores e uso de proxies.
- **Desempenho:** Estratégias como `SEQUENCE` e `TABLE` podem oferecer melhor desempenho e consistência em alguns
  cenários.
- **Portabilidade:** O uso de `SEQUENCE` garante um alto grau de portabilidade entre diferentes bancos de dados.

### @NaturalId()







