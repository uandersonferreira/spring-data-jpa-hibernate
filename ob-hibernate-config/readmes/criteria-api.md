# Criteria Api

>> **Obervação:** Conteúdo gerado por IA, consultar documentação oficial para tirar dúvidas 
- documentação: https://docs.jboss.org/hibernate/core/4.0/hem/en-US/html/querycriteria.html 

O Criteria API do JPA/Hibernate é uma API de consulta que permite construir consultas de maneira programática, usando a linguagem Java em vez de SQL ou HQL. Isso oferece várias vantagens, como a verificação de tipos em tempo de compilação e a facilidade de construção dinâmica de consultas.

Aqui está uma visão geral do Criteria API e seus principais métodos:

### Principais Componentes do Criteria API

1. **`CriteriaBuilder`**: É a fábrica para instâncias de `CriteriaQuery`, `Predicate`, `Order`, e outros tipos de consulta. Você obtém uma instância de `CriteriaBuilder` a partir da `Session` ou `EntityManager`.

2. **`CriteriaQuery<T>`**: Representa a consulta SQL em si. Você cria uma instância de `CriteriaQuery` usando o `CriteriaBuilder`. 
Ele armazena o que você quer selecionar, de qual entidade, com quais condições e ordenações.
 > Equivalente SQL: A consulta completa (SELECT ... FROM ... WHERE ... ORDER BY ...).

3. **`Root<T>`**: O Root<T> especifica a entidade principal que será usada na consulta, ou seja, o "root" da consulta. É semelhante à cláusula FROM em SQL, onde você define de qual tabela (entidade) os dados serão extraídos

> Equivalente SQL: Cláusula FROM.


4. **`Predicate`**: Representa uma condição na consulta. É semelhante às cláusulas `WHERE` em SQL que filtram os resultados.
> Por exemplo, cb.equal(root.get("nome"), "Uanderson") cria um Predicate que diz: "somente traga resultados onde o nome seja 'Uanderson'".

5. **`Path<X>`**: O Path<X> permite navegar dentro de uma entidade e acessar seus atributos. É semelhante a acessar colunas específicas em SQL. Quando você faz root.get("nome"), está acessando o campo nome da entidade, como faria com uma coluna em SQL.

> Equivalente SQL: Acesso aos atributos/colunas (e.g., SELECT **nome** FROM Tabela).

6. **`Order`**: Representa a ordenação dos resultados da consulta.
> Equivalente SQL: Cláusula ORDER BY.

7. **`Expression<T>`**: Representa uma expressão que pode ser usada em operações, como somas, médias, máximos, mínimos, contagens e outras funções matemáticas ou lógicas. No SQL, isso equivale a usar funções como AVG(), SUM(), COUNT(), MIN(), MAX(), ou até mesmo expressões mais complexas como age * 2.

### Exemplo de Uso do Criteria API

Abaixo está um exemplo que mostra como usar o Criteria API para consultar a entidade `Employee`:

```java
// Obtém a sessão do Hibernate
Session session = HibernateUtil.getSessionFactory().openSession();

// Obtém o CriteriaBuilder a partir da sessão
CriteriaBuilder builder = session.getCriteriaBuilder();

// Cria uma CriteriaQuery para a entidade Employee
CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);

// Define a raiz da consulta (a entidade a ser consultada)
Root<Employee> root = criteria.from(Employee.class);

// Adiciona uma condição à consulta (equivalente a WHERE)
criteria.select(root).where(builder.equal(root.get("firstName"), "John"));

// Executa a consulta e obtém os resultados
List<Employee> employees = session.createQuery(criteria).getResultList();
```

### Principais Métodos do CriteriaBuilder

- **`createQuery(Class<T> resultClass)`**: Cria uma nova instância de `CriteriaQuery` para o tipo especificado.
- **`equal(Expression<?> x, Object y)`**: Cria uma expressão de igualdade.
- **`like(Expression<String> x, String pattern)`**: Cria uma expressão de correspondência de padrão.
- **`greaterThan(Expression<? extends Number> x, Number y)`**: Cria uma expressão de comparação "maior que".
- **`lessThan(Expression<? extends Number> x, Number y)`**: Cria uma expressão de comparação "menor que".
- **`and(Predicate... restrictions)`**: Combina várias expressões usando AND.
- **`or(Predicate... restrictions)`**: Combina várias expressões usando OR.
- **`asc(Expression<?> x)`**: Define a ordenação ascendente para a expressão especificada.
- **`desc(Expression<?> x)`**: Define a ordenação descendente para a expressão especificada.

### Principais Métodos do CriteriaQuery

- **`select(Selection<? extends X> selection)`**: Define o que deve ser retornado pela consulta.
- **`where(Predicate... restrictions)`**: Define as condições da consulta.
- **`orderBy(Order... o)`**: Define a ordenação dos resultados da consulta.
- **`groupBy(Expression<?>... grouping)`**: Define a agrupamento dos resultados da consulta.
- **`multiselect(Selection<?>... selections)`**: Selecionar múltiplos campos.

### Exemplo Avançado

A seguir, um exemplo mais avançado que inclui ordenação e múltiplas condições:

```java
// Obtém a sessão do Hibernate
Session session = HibernateUtil.getSessionFactory().openSession();

// Obtém o CriteriaBuilder a partir da sessão
CriteriaBuilder builder = session.getCriteriaBuilder();

// Cria uma CriteriaQuery para a entidade Employee
CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);

// Define a raiz da consulta (a entidade a ser consultada)
Root<Employee> root = criteria.from(Employee.class);

// Adiciona múltiplas condições (equivalente a WHERE firstName = 'John' AND age > 30)
Predicate firstNamePredicate = builder.equal(root.get("firstName"), "John");
Predicate agePredicate = builder.greaterThan(root.get("age"), 30);
criteria.select(root).where(builder.and(firstNamePredicate, agePredicate));

// Adiciona ordenação (equivalente a ORDER BY lastName ASC)
criteria.orderBy(builder.asc(root.get("lastName")));

// Executa a consulta e obtém os resultados
List<Employee> employees = session.createQuery(criteria).getResultList();
```

Com o Criteria API, você pode construir consultas complexas de forma programática, beneficiando-se da verificação de tipos e da flexibilidade da linguagem Java.

### Passos para Definir uma Consulta com Criteria

1. **Obter o `Session` ou `EntityManager`**: Dependendo se você está usando Hibernate puro ou JPA.
2. **Obter o `CriteriaBuilder`**: A partir do `Session` ou `EntityManager`.
3. **Criar uma instância de `CriteriaQuery`**: Para a entidade que você deseja consultar.
4. **Definir a raiz da consulta (`Root<T>`)**: Especificando a entidade principal da consulta.
5. **Construir Predicados (Condições)**: Usar `CriteriaBuilder` para definir as condições (`WHERE`).
6. **Definir Seleção**: Especificar o que deve ser retornado pela consulta.
7. **Ordenar os Resultados (Opcional)**: Especificar a ordem dos resultados.
8. **Executar a Consulta**: Usar a instância de `Query` para executar a consulta e obter os resultados.

### Estrutura Básica

Aqui está a estrutura básica para criar uma consulta usando Criteria:

```java
// Passo 1: Obter a sessão do Hibernate
Session session = HibernateUtil.getSessionFactory().openSession();

// Passo 2: Obter o CriteriaBuilder
CriteriaBuilder builder = session.getCriteriaBuilder();

// Passo 3: Criar uma CriteriaQuery para a entidade desejada
CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);

// Passo 4: Definir a raiz da consulta
Root<Employee> root = criteria.from(Employee.class);

// Passo 5: Adicionar condições (Predicados)
Predicate condition1 = builder.equal(root.get("firstName"), "John");
Predicate condition2 = builder.greaterThan(root.get("age"), 30);
criteria.where(builder.and(condition1, condition2));

// Passo 6: Definir a seleção (o que deve ser retornado)
criteria.select(root);

// Passo 7: Adicionar ordenação (opcional)
criteria.orderBy(builder.asc(root.get("lastName")));

// Passo 8: Executar a consulta e obter os resultados
List<Employee> employees = session.createQuery(criteria).getResultList();
```

### Exemplos de Uso

#### Consulta Simples com Condição

```java
// Condição: firstName = 'John'
Predicate condition = builder.equal(root.get("firstName"), "John");
criteria.where(condition);
```

#### Consulta com Múltiplas Condições

```java
// Condições: firstName = 'John' AND age > 30
Predicate condition1 = builder.equal(root.get("firstName"), "John");
Predicate condition2 = builder.greaterThan(root.get("age"), 30);
criteria.where(builder.and(condition1, condition2));
```

#### Consulta com Ordenação

```java
// Ordenação: ORDER BY lastName ASC
criteria.orderBy(builder.asc(root.get("lastName")));
```

#### Consulta com Seleção Específica

```java
// Selecionar apenas alguns campos (por exemplo, firstName e lastName)
criteria.multiselect(root.get("firstName"), root.get("lastName"));
```

## Qual a relação entre JPA, Criteria API e Hibernate ?

### JPA (Java Persistence API)

**O que é**:
- JPA é uma especificação da Java EE (Enterprise Edition) para gerenciamento de persistência e mapeamento objeto-relacional (ORM).
- Ele define um conjunto de APIs que padronizam a forma como objetos Java são mapeados para bancos de dados relacionais.

**O que inclui**:
- **EntityManager**: Interface principal para interagir com a unidade de persistência.
- **Annotations**: Para mapear classes Java para tabelas de banco de dados (ex: `@Entity`, `@Table`, `@Id`).
- **JPQL (Java Persistence Query Language)**: Linguagem de consulta similar ao SQL mas orientada a objetos.
- **Criteria API**: API para construir consultas de forma programática.

### Hibernate

**O que é**:
- Hibernate é uma implementação do JPA. É um framework ORM que fornece sua própria implementação das especificações JPA, além de funcionalidades adicionais.
- Hibernate pode ser usado tanto como uma implementação JPA quanto de forma nativa, sem JPA.

**O que inclui**:
- **Session**: Interface principal do Hibernate para interagir com o banco de dados (similar ao `EntityManager` do JPA).
- **HQL (Hibernate Query Language)**: Linguagem de consulta própria do Hibernate.
- **Criteria API do Hibernate**: Versão própria do Hibernate para construir consultas de forma programática (anterior ao JPA 2.0).

### Criteria API

**O que é**:
- Criteria API é uma forma de construir consultas de forma programática usando o Java, em vez de usar strings de consulta como JPQL ou HQL.
- Foi introduzida no JPA 2.0 para fornecer uma alternativa segura em tempo de compilação para construir consultas.

**Como se relaciona**:
- **JPA Criteria API**: Parte da especificação JPA e pode ser usada com qualquer implementação JPA, incluindo Hibernate.
- **Hibernate Criteria API**: Uma API específica do Hibernate anterior ao JPA 2.0. Embora ainda seja suportada, o uso da Criteria API do JPA é recomendado para projetos novos.

### Relação entre eles

1. **JPA é a especificação**:
    - Define como deve ser o mapeamento ORM e a interação com o banco de dados.
    - Inclui a Criteria API como parte da especificação.

2. **Hibernate é uma implementação da JPA**:
    - Implementa todas as especificações definidas pelo JPA.
    - Adiciona funcionalidades extras que não estão no JPA.
    - Pode ser usado tanto com JPA quanto de forma nativa.

3. **Criteria API é uma forma de construir consultas**:
    - **JPA Criteria API**: Funciona com qualquer implementação JPA (como Hibernate).
    - **Hibernate Criteria API**: Uma implementação própria do Hibernate para construir consultas (mais antiga e específica do Hibernate).

### Resumo

- **JPA**: Especificação que define como deve ser a interação ORM.
- **Hibernate**: Implementação do JPA (e mais), fornecendo ferramentas adicionais e suporte para o JPA Criteria API.
- **Criteria API**: Forma de construir consultas de forma programática; parte da especificação JPA e implementada pelo Hibernate e outros fornecedores JPA.

## Como diferenciar cada implementação ?

Para diferenciar o uso da Criteria API do Hibernate nativo e a Criteria API do JPA, é importante entender que cada um tem sua própria abordagem para a construção de consultas programáticas. Aqui estão as principais diferenças entre as duas:

### Hibernate Criteria API (nativa)

**Contexto**:
- Antes do JPA 2.0, o Hibernate oferecia sua própria API de Criteria para construção de consultas programáticas.
- Essa API ainda é suportada, mas é recomendável usar a Criteria API do JPA para novos projetos.

**Sintaxe**:
- Usa classes específicas do Hibernate, como `Criteria`, `Restrictions`, `Projections`, etc.

**Exemplo**:

```java
// Obter a sessão do Hibernate
Session session = HibernateUtil.getSessionFactory().openSession();

// Criar uma Criteria para a entidade Employee
Criteria criteria = session.createCriteria(Employee.class);

// Adicionar restrições
criteria.add(Restrictions.eq("firstName", "John"));
criteria.add(Restrictions.gt("age", 30));

// Executar a consulta e obter os resultados
List<Employee> employees = criteria.list();
```

### JPA Criteria API

**Contexto**:
- Introduzida no JPA 2.0 como parte da especificação.
- Fornece uma maneira padrão e agnóstica de fornecedor para construir consultas programáticas.

**Sintaxe**:
- Usa classes e interfaces da API JPA (`CriteriaBuilder`, `CriteriaQuery`, `Root`, `Predicate`, etc.).
- Pode ser usada com qualquer implementação JPA, incluindo Hibernate.

**Exemplo**:

```java
// Obter o EntityManager ou Session
// Session session = HibernateUtil.getSessionFactory().openSession();
EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");//olhar o outro projeto feito 
EntityManager em = emf.createEntityManager();

// Obter o CriteriaBuilder
CriteriaBuilder cb = em.getCriteriaBuilder();

// Criar uma CriteriaQuery para a entidade Employee
CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
Root<Employee> root = cq.from(Employee.class);

// Adicionar restrições
Predicate firstNameCondition = cb.equal(root.get("firstName"), "John");
Predicate ageCondition = cb.greaterThan(root.get("age"), 30);
cq.where(cb.and(firstNameCondition, ageCondition));

// Definir a seleção
cq.select(root);

// Executar a consulta e obter os resultados
List<Employee> employees = em.createQuery(cq).getResultList();
```

### Principais Diferenças

1. **Classes Utilizadas**:
    - **Hibernate Criteria API**: Usa `Criteria`, `Restrictions`, `Projections`, etc.
    - **JPA Criteria API**: Usa `CriteriaBuilder`, `CriteriaQuery`, `Root`, `Predicate`, etc.

2. **Portabilidade**:
    - **Hibernate Criteria API**: Específica do Hibernate. Não é portátil para outras implementações JPA.
    - **JPA Criteria API**: Parte da especificação JPA e pode ser usada com qualquer provedor JPA.

3. **Compatibilidade com Padrões**:
    - **Hibernate Criteria API**: Específica do Hibernate e não segue a especificação JPA.
    - **JPA Criteria API**: Segue a especificação JPA, proporcionando consistência e compatibilidade entre diferentes provedores JPA.


## Exemplo da implementação com SQL, HQL E CRITERIA API

Aqui estão exemplos de métodos para executar uma consulta simples na tabela `Employee` pelo `id`, utilizando SQL, HQL e Criteria API. Vou mostrar apenas a parte da construção da consulta dentro de um método.


### 1. Consulta com SQL
Neste exemplo, usamos uma consulta nativa SQL:

```java
public Employee findEmployeeByIdSQL(Long id) {
    EntityManager em = getEntityManager();
    String sql = "SELECT * FROM Employee WHERE id = :id";
    Query query = em.createNativeQuery(sql, Employee.class);
    query.setParameter("id", id);
    return (Employee) query.getSingleResult();
}
```

### 2. Consulta com HQL (Hibernate Query Language)
Neste exemplo, usamos HQL, que é a linguagem de consulta orientada a objetos do Hibernate:

```java
public Employee findEmployeeByIdHQL(Long id) {
    EntityManager em = getEntityManager();
    String hql = "FROM Employee WHERE id = :id";
    Query query = em.createQuery(hql);
    query.setParameter("id", id);
    return (Employee) query.getSingleResult();
}
```

### 3. Consulta com Criteria API
Neste exemplo, usamos a Criteria API para construir a consulta de forma programática:

```java
public Employee findEmployeeByIdCriteria(Long id) {
    EntityManager em = getEntityManager();
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);
    Root<Employee> root = criteria.from(Employee.class);
    criteria.select(root).where(builder.equal(root.get("id"), id));
    return em.createQuery(criteria).getSingleResult();
}
```

### Resumo das Diferenças

1. **SQL**:
    - Usamos uma consulta SQL nativa.
    - A consulta é escrita como uma string SQL.
    - Boa para consultas complexas que não são facilmente expressas em HQL ou Criteria.

2. **HQL**:
    - Usamos uma consulta HQL, que é similar ao SQL, mas orientada a objetos.
    - Referenciamos a entidade `Employee` diretamente.
    - Boa para quem está familiarizado com SQL, mas deseja trabalhar com entidades.

3. **Criteria API**:
    - Usamos a API programática para construir a consulta.
    - Utiliza o `CriteriaBuilder` e `CriteriaQuery`.
    - Boa para consultas dinâmicas e para aproveitar a verificação de tipos em tempo de compilação.
