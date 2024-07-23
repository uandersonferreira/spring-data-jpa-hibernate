```xml

<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             version="3.0"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">
    <persistence-unit name="my-persistence-unit" transaction-type="RESOURCE_LOCAL">
        <description>Project for learning Hibernate</description>

        <!-- Persistence provider implementation -->
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <!-- Exclude unlisted classes -->
        <exclude-unlisted-classes>false</exclude-unlisted-classes>

        <!-- Database connection properties -->
        <properties>
            <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/db_hibernate"/>
            <property name="jakarta.persistence.jdbc.user" value="test"/>
            <property name="jakarta.persistence.jdbc.password" value="test123"/>
        </properties>

    </persistence-unit>
</persistence>
```

### Estrutura do Arquivo

1. **Declaração do Documento XML**
    ```xml
    <persistence xmlns="https://jakarta.ee/xml/ns/persistence"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 version="3.0"
                 xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd">
    ```
    - **xmlns**: Define o namespace XML padrão para o documento, que aponta para a especificação de persistência
      Jakarta.
    - **xmlns:xsi**: Define o namespace XML Schema Instance, utilizado para especificar o esquema do documento XML.
    - **version**: Especifica a versão da especificação de persistência Jakarta utilizada.
    - **xsi:schemaLocation**: Fornece a localização do esquema XML para validação do documento.

2. **Unidade de Persistência**
    ```xml
    <persistence-unit name="my-persistence-unit" transaction-type="RESOURCE_LOCAL">
    ```
    - **name**: Nome da unidade de persistência. Este nome é usado para referenciar a unidade de persistência no código
      Java.
    - **transaction-type**: Tipo de transação usada. `RESOURCE_LOCAL` indica que a transação será gerenciada localmente,
      ou seja, não usará um gerenciador de transações JTA.

3. **Descrição**
    ```xml
    <description>Project for learning Hibernate</description>
    ```
    - Uma descrição opcional da unidade de persistência.

4. **Provedor de Persistência**
    ```xml
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    ```
    - Especifica o provedor de persistência a ser utilizado. Neste caso, é o `HibernatePersistenceProvider` do
      Hibernate.

5. **Excluir Classes Não Listadas**
    ```xml
    <exclude-unlisted-classes>false</exclude-unlisted-classes>
    ```
    - Define se classes não listadas explicitamente devem ser incluídas no gerenciamento da unidade de
      persistência. `false` indica que todas as classes da aplicação que estão anotadas com `@Entity` serão incluídas,
      mesmo que não estejam listadas explicitamente.

6. **Propriedades de Conexão com o Banco de Dados**
    ```xml
    <properties>
        <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
        <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/db_hibernate"/>
        <property name="jakarta.persistence.jdbc.user" value="test"/>
        <property name="jakarta.persistence.jdbc.password" value="test123"/>
    </properties>
    ```
    - **jakarta.persistence.jdbc.driver**: Especifica o driver JDBC a ser utilizado. Neste caso, é o driver do
      PostgreSQL.
    - **jakarta.persistence.jdbc.url**: URL de conexão com o banco de dados. Aqui, está configurado para conectar ao
      banco `db_hibernate` no `localhost` na porta `5432`.
    - **jakarta.persistence.jdbc.user**: Nome de usuário para se conectar ao banco de dados.
    - **jakarta.persistence.jdbc.password**: Senha para se conectar ao banco de dados.

   
A diferença entre `RESOURCE_LOCAL` e `JTA` no contexto da JPA está relacionada à forma como as transações são gerenciadas. Vamos detalhar cada um:

### `RESOURCE_LOCAL`

- **Transação Local**: O tipo `RESOURCE_LOCAL` indica que as transações são gerenciadas localmente pelo próprio `EntityManager`. Isso significa que cada instância de `EntityManager` controla suas próprias transações.
- **Contexto de Aplicação**: É comumente usado em aplicações stand-alone, como aplicações desktop ou aplicações Java SE que não utilizam um servidor de aplicações Java EE ou Jakarta EE.
- **Gerenciamento de Transações**: O desenvolvedor é responsável por iniciar e finalizar as transações explicitamente usando `entityManager.getTransaction().begin()` e `entityManager.getTransaction().commit()` ou `rollback()`.
- **Simplicidade**: É mais simples de configurar e gerenciar, adequado para aplicações que não precisam de coordenação de transações distribuídas ou globais.

#### Exemplo:

```xml
<persistence-unit name="my-persistence-unit" transaction-type="RESOURCE_LOCAL">
    <!-- Configurações -->
</persistence-unit>
```

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
// Operações de persistência
em.getTransaction().commit();
em.close();
emf.close();
```

### `JTA` (Java Transaction API)

- **Transação Gerenciada**: O tipo `JTA` indica que as transações são gerenciadas por um provedor de transações externo, geralmente um servidor de aplicações que suporta Java EE ou Jakarta EE.
- **Contexto de Aplicação**: É utilizado em ambientes corporativos onde há necessidade de coordenação de transações distribuídas entre múltiplos recursos, como vários bancos de dados, sistemas de mensagens, etc.
- **Gerenciamento de Transações**: As transações são gerenciadas pelo contêiner (como um servidor de aplicações), e o desenvolvedor geralmente não precisa iniciar ou finalizar as transações explicitamente. Em vez disso, o contêiner coordena as transações declaradas através de anotações (`@Transactional`) ou configurações no deployment descriptor.
- **Coordenação de Transações Distribuídas**: Suporta transações distribuídas (também chamadas de transações globais) que podem abranger múltiplos recursos, proporcionando consistência entre diferentes sistemas.

#### Exemplo:

```xml
<persistence-unit name="my-persistence-unit" transaction-type="JTA">
    <!-- Configurações -->
</persistence-unit>
```

```java
// Dentro de um EJB ou outro componente gerenciado pelo contêiner
@Stateless
public class MyService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void doSomething() {
        // Operações de persistência
    }
}
```

### Resumo

- **RESOURCE_LOCAL**:
   - Gerenciamento de transações locais pelo `EntityManager`.
   - Adequado para aplicações stand-alone.
   - Desenvolvedor gerencia as transações explicitamente.

- **JTA**:
   - Gerenciamento de transações pelo contêiner.
   - Adequado para aplicações corporativas que utilizam servidores de aplicação.
   - Suporte para transações distribuídas.
   - Transações geralmente são declarativas e coordenadas pelo contêiner.

A escolha entre `RESOURCE_LOCAL` e `JTA` depende do ambiente de execução da aplicação e dos requisitos de transação. Para aplicações simples, `RESOURCE_LOCAL` pode ser suficiente, enquanto que `JTA` é mais adequado para ambientes corporativos complexos que requerem transações distribuídas.

### Resumo

Este arquivo `persistence.xml` configura uma unidade de persistência chamada
`my-persistence-unit` para um projeto de aprendizado de Hibernate, utilizando
transações locais e especificando detalhes de conexão para um banco de dados PostgreSQL. O Hibernate é configurado como
o provedor de persistência e todas as classes anotadas com `@Entity` na aplicação serão incluídas no gerenciamento da
unidade de persistência.