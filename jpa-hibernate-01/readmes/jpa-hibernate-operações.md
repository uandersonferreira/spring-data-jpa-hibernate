## Principais métodos do JPA/Hibernate

### 1. `entityManager.persist(entity)`

**Descrição:** O método `persist` é usado para salvar uma nova entidade no banco de dados. Ele faz a entidade transitar
do estado "novo" para o estado "gerenciado" no contexto de persistência.

**Uso:**

```java
Student student = new Student();
student.

setName("Uanderson Oliveira");
entityManager.

persist(student);
```

**SQL Correspondente:**

```sql
INSERT INTO Student (name)
VALUES ('Uanderson Oliveira');
```

**Explicação:** Quando você chama `persist`, a entidade é salva no banco de dados na próxima sincronização do contexto
de persistência com o banco de dados, que geralmente acontece quando a transação é comitada.

```java
public class Main {
    public static void main(String[] args) {
//Define as propriedades do hibernate
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "true"); // Mostrar as queries SQL geradas pelo Hibernate
        properties.put("hibernate.format_sql", "true"); // Mostrar as queries SQL formatadas

        // Criar a fábrica de EntityManager (EntityManagerFactory)
        try (EntityManagerFactory entityManagerFactory =
                     new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                             new MyPersistenceUnitInfo(), properties)) {

            /* EntityManager -> Persistence Context */
            // Criar o EntityManager que gerencia o contexto de persistência
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

                // Iniciar uma transação
                entityManager.getTransaction().begin();

//                // Criar uma nova instância da entidade Student (NEW)
//                Student student1 = new Student();
//
//                // Definir o ID do estudante
//                student1.setId(1L);
//
//                // Definir o nome do estudante
//                student1.setName("Uanderson Oliveira");
//
//                // Persistir/Passar a existir no contexto de persistẽncia pois queremos (salvar)
//                // a entidade student1 no banco de dados
//EQUIVALE AO INSERT DO SQL
//                entityManager.persist(student1);


                //find - EQUIVALE AO SELECT COM WHERE DO SQL, Recupera o registro do database e insere no contexto de persistência
                Student student1 = entityManager.find(Student.class, 1L);

                //Mesmo modificando, aqui os objetos continuam sendo o mesmo, pois tem o mesmo id
                //Essa mudança, somente será persistida, quando a transação terminar (IMPORTANTE)
                // TRANSACTION - COMEÇO | MEIO | FIM - Tudo ou nada - Principio da Atomicidade.
                student1.setName("NOVO NOME");


                //Por estamos buscando um registro com o id, que já existe no contexto de persitência
                //Irá somente nos retornar esse registro, Não irá fazer um novo SELECT COM WHERE DO SQL
                Student student2 = entityManager.find(Student.class, 1L);

                //Mesmo tendo alterado o nome em cima, irá trazer o último registro salvo, antes dessa transação ser finalizada.
                System.out.println(student1);

                //Ambas referências apontam para o mesmo object em memória;
                System.out.println("isTrue: " + (student1 == student2));

                //isso irá gerar um UPDATE DO SQL, pois a transação ainda não foi finalizada/comitada
                //student1.setName("Uanderson Oliveira 2");

                // Confirmar a transação (salvar as alterações no banco de dados)
                //Sincronização das transações no contexto de persistência com o database(DBMS)
                entityManager.getTransaction().commit();
            }

        }

    }//main

}//class
```

### 2. `entityManager.find(entityClass, primaryKey)`

**Descrição:** O método `find` (nosso `fetch = FetchType.EAGER` do spring) é usado para buscar uma entidade no banco de
dados com base na sua chave primária. Se a
entidade for encontrada, ela é retornada e colocada no contexto de persistência.

**Uso:**

```java
Student student = entityManager.find(Student.class, 1L);
```

**SQL Correspondente:**

```sql
SELECT *
FROM Student
WHERE id = 1;
```

**Explicação:** Este método realiza uma consulta no banco de dados para buscar a entidade com o ID especificado e a
coloca no contexto de persistência.

### getReference (LAZY LOAD)  vs Find (EAGER LOAD)

### `entityManager.getReference()`

**Descrição:** O método `getReference` nosso (`fetch = FetchType.LAZY` do spring) é usado para obter uma referência "
proxy" para a entidade. Ele não faz uma consulta imediata ao banco de dados, mas retorna um proxy da entidade, que será
carregado sob demanda.

**Uso:**

```java
Student studentWithGetReference = entityManager.getReference(Student.class, 1L);
```

**SQL Correspondente:** Nenhum SQL imediato.

**Explicação:** Quando você chama `getReference`, o JPA não executa imediatamente uma consulta no banco de dados. Em vez
disso, ele retorna um proxy da entidade, que será carregado com os dados do banco de dados apenas quando for necessário
acessar uma propriedade da entidade. Isso pode melhorar o desempenho em certos cenários, já que evita uma consulta
desnecessária ao banco de dados até que realmente seja necessário acessar os dados da entidade.

### Diferenças Detalhadas:

1. **Tempo de Execução da Consulta:**
    - `find`: Executa a consulta no banco de dados imediatamente.
    - `getReference`: Não executa a consulta no banco de dados imediatamente; retorna um proxy.

2. **Estado da Entidade:**
    - `find`: Retorna a entidade totalmente carregada e gerenciada pelo contexto de persistência.
    - `getReference`: Retorna um proxy que será carregado sob demanda.

3. **Comportamento com Entidades Não Existentes:**
    - `find`: Retorna `null` se a entidade não for encontrada no banco de dados.
    - `getReference`: Lança uma `EntityNotFoundException` quando o proxy é acessado, se a entidade não for encontrada no
      banco de dados.

4. **Uso Típico:**
    - `find`: Usado quando você precisa da entidade completa imediatamente.
    - `getReference`: Usado quando você precisa apenas de uma referência à entidade e deseja adiar o carregamento real
      dos dados até que seja realmente necessário.

### Exemplo Prático:

```java
// Usando find
Student studentWithFind = entityManager.find(Student.class, 1L);
// A consulta SQL é executada imediatamente

// Usando getReference
Student studentWithGetReference = entityManager.getReference(Student.class, 1L);
// Nenhuma consulta SQL é executada imediatamente
// A consulta SQL será executada somente quando você tentar acessar uma propriedade da entidade
// Por exemplo:
System.out.

println(studentWithGetReference.getName()); // Aqui a consulta será executada
```

### Resumo

- **`find`**:
    - Executa a consulta SQL imediatamente.
    - Retorna a entidade carregada e gerenciada.
    - SQL: `SELECT * FROM Student WHERE id = 1;`

- **`getReference`**:
    - Retorna um proxy sem executar a consulta SQL imediatamente.
    - Consulta SQL executada somente quando necessário.
    - SQL: Executa `SELECT` sob demanda ao acessar propriedades da entidade.

OBSERVAÇÃO:

Um proxy em termos de programação é um objeto que atua como um substituto ou representante de outro objeto. Ele controla
o acesso ao objeto real, permitindo operações como adiar a execução de ações até que sejam realmente necessárias, ou
adicionar funcionalidades adicionais como segurança ou logging.

Para entender melhor, vamos fazer uma analogia com o mundo real.

### Analogia com o Mundo Real

Imagine que você tem um escritório de advocacia, e você precisa falar com um advogado especializado em uma área
específica. Esse advogado está muito ocupado e não pode ser interrompido para qualquer coisa pequena. Então, em vez de
falar diretamente com ele, você tem um assistente (proxy) que pode lidar com muitas de suas perguntas e apenas chama o
advogado quando realmente necessário.

#### Cenário Sem Proxy

- **Você**: Vai diretamente ao advogado para cada pergunta.
- **Advogado**: Precisa parar o que está fazendo e responder cada pergunta imediatamente.

#### Cenário Com Proxy

- **Você**: Vai ao assistente (proxy) com suas perguntas.
- **Assistente (Proxy)**: Tenta responder suas perguntas. Se for algo que o assistente pode responder, ele lida com isso
  imediatamente.
- **Advogado**: Só é interrompido pelo assistente quando há uma pergunta que o assistente não pode responder.

### Aplicação ao `getReference`

No contexto do JPA e Hibernate:

- **`find`**: É como ir diretamente ao advogado. O JPA executa a consulta no banco de dados imediatamente e retorna a
  entidade completa.
- **`getReference`**: É como ir ao assistente. O JPA retorna um proxy (assistente) que parece ser a entidade. O proxy
  não carrega imediatamente os dados do banco de dados, mas adia essa ação até que você realmente precise de alguma
  informação da entidade.

### Como o Proxy Funciona no JPA/Hibernate

1. **Criação do Proxy**:
    - Quando você usa `getReference`, o JPA cria um proxy para a entidade em vez de carregar imediatamente todos os
      dados do banco de dados.

2. **Interação com o Proxy**:
    - O proxy age como um substituto. Ele tem o mesmo tipo da entidade e pode ser usado como se fosse a entidade real.
    - Quando você chama um método no proxy que requer acessar dados da entidade (como `getName`), o proxy realiza a
      consulta ao banco de dados naquele momento para carregar os dados reais.

### 3. `entityManager.refresh(entity)`

**Descrição:** O método `refresh` no JPA/Hibernate é usado para sincronizar o estado da entidade com o estado do banco
de dados. Isso
significa que quaisquer alterações feitas no contexto de persistência que ainda não foram comitadas serão descartadas e
a entidade será "recarregada" do banco de dados.

**Uso:**

```java
Student student = entityManager.find(Student.class, 1L);
student.

setName("Nome Temporário");
entityManager.

refresh(student);
```

**SQL Correspondente:**

```sql
SELECT *
FROM Student
WHERE id = 1;
```

**Explicação:** Quando você chama `refresh`, quaisquer alterações feitas na entidade desde a última sincronização são
descartadas e a entidade é recarregada com os dados atuais do banco de dados.

```java
public class FindAndRefresh {
    public static void main(String[] args) {
        //Define as propriedades do hibernate
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "true"); // Mostrar as queries SQL geradas pelo Hibernate
        properties.put("hibernate.format_sql", "true"); // Mostrar as queries SQL formatadas
        properties.put("hibernate.hbm2ddl.auto", "none");

        // Criar a fábrica de EntityManager (EntityManagerFactory)
        try (EntityManagerFactory entityManagerFactory =
                     new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                             new MyPersistenceUnitInfo(), properties)) {

            /* EntityManager -> Persistence Context */
            // Criar o EntityManager que gerencia o contexto de persistência
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

                // Iniciar uma transação
                entityManager.getTransaction().begin();

                // Buscar uma entidade Student do banco de dados - EQUIVALE AO SELECT DO SQL
                Student student = entityManager.find(Student.class, 1L);

                // Alterar o nome da entidade Student
                student.setName("Uanderson Oliveira 2");

                //O entityManager Usar o método refresh para sincronizar a entidade com o estado do banco de dados, então qualquer alteração
                //feita anteriormente é descartada pelo method, pois ele pega os dados mais atualziados já persistidos no banco
                //de dados.OBS. ASSIM COMO O FIND ELE EXECUTA UM SELECT DO SQL, JÁ QUE RETORNA OS DADOS SALVOS NO MESMO.
                entityManager.refresh(student);

                // Imprimir o estado da entidade após o refresh (com os dados do banco de dados e não com os das alterações)
                System.out.println(student);

                // Confirmar a transação (salvar as alterações no banco de dados)
                entityManager.getTransaction().commit();
            }
        }

    }//main

}//class
```

### 4. `entityManager.merge(entity)`

**Descrição:** O método `merge` é usado para sincronizar o estado de uma entidade desanexada (ou uma nova entidade) com
o contexto de persistência. Ele retorna uma instância gerenciada da entidade, refletindo o estado persistido no banco de
dados. Se a entidade já existir no banco de dados, ela é atualizada; se não, uma nova entidade é inserida.

**Uso:**

```java
Student student = new Student();
student.setId(1L);
student.setName("Uanderson merged");

Student mergedStudent = entityManager.merge(student);
```

**SQL Correspondente:** Dependendo do estado da entidade, a operação pode resultar em um `UPDATE` ou um `INSERT`.

### Exemplo de Comportamento

```java
// Criar a fábrica de EntityManager (EntityManagerFactory)
try(EntityManagerFactory entityManagerFactory =
        new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                new MyPersistenceUnitInfo(), properties)){

        // Criar o EntityManager que gerencia o contexto de persistência
        try(
EntityManager entityManager = entityManagerFactory.createEntityManager()){

        // Iniciar uma transação
        entityManager.

getTransaction().

begin();

// Criar uma nova entidade Student
Student student = new Student();
        student.

setId(1L);
        student.

setName("Uanderson merged");

// Usar merge para sincronizar a entidade com o contexto de persistência
Student mergedStudent = entityManager.merge(student);

// Confirmar a transação (salvar as alterações no banco de dados)
        entityManager.getTransaction().commit();

// A entidade student é impressa, e a alteração foi salva no banco de dados
        System.out.println(mergedStudent);
        
        }
            }
```

**Passos:**

1. Uma nova instância de `Student` é criada e suas propriedades são definidas.
2. O método `merge` é chamado, o que faz com que o estado da entidade desanexada `student` seja sincronizado com o
   contexto de persistência.
3. Se a entidade `Student` com `id` 1 já existir no banco de dados, ela é atualizada com o novo nome "Uanderson merged".
   Se não existir, uma nova entidade é inserida.
4. A transação é confirmada (`commit`), persistindo a alteração no banco de dados.
5. A entidade `mergedStudent` refletirá o estado gerenciado da entidade após a operação de `merge`.

**SQL Correspondente:**

- Se a entidade já existe no banco de dados, um `UPDATE` é gerado:
  ```sql
  UPDATE student SET name = 'Uanderson merged' WHERE id = 1;
  ```
- Se a entidade não existe, um `INSERT` é gerado:
  ```sql
  INSERT INTO student (id, name) VALUES (1, 'Uanderson merged');
  ```

### Comportamento do `merge`

**Estado da Entidade Antes do `merge`:**

- **Desanexada (Detached):** Se a entidade estava previamente no contexto de persistência e foi desanexada, o `merge`
  trará a entidade de volta ao contexto, atualizando seu estado no banco de dados.
- **Nova (New):** Se a entidade é nova (nunca foi persistida), o `merge` persistirá a nova entidade no banco de dados.

**Após o `merge`:**

- A instância retornada pelo `merge` está no estado gerenciado. As mudanças feitas nesta instância serão rastreadas
  pelo `EntityManager` e sincronizadas com o banco de dados quando a transação for confirmada.
- A instância original passada para o `merge` não é gerenciada. Qualquer mudança feita nesta instância após o `merge`
  não será rastreada pelo `EntityManager`.

### Considerações Importantes:

- **`merge` vs. `persist`:** Use `merge` para sincronizar entidades desanexadas ou novas com o contexto de persistência.
  Use `persist` apenas para novas entidades.
- **Retorno do `merge`:** Sempre trabalhe com a instância retornada pelo `merge`, pois esta é a instância gerenciada
  pelo `EntityManager`.
- **Efeito no Banco de Dados:** O comportamento do `merge` no banco de dados dependerá se a entidade já existe ou não,
  resultando em um `UPDATE` ou `INSERT`.

### 5. `entityManager.remove(entity)`

**Descrição:** O método `remove` é usado para excluir uma entidade gerenciada do banco de dados.

**Uso:**

```java
Student student = entityManager.find(Student.class, 1L);
entityManager.

remove(student);
```

**SQL Correspondente:**

```sql
DELETE
FROM Student
WHERE id = 1;
```

**Explicação:** Quando você chama `remove`, a entidade é marcada para remoção e será excluída do banco de dados na
próxima sincronização do contexto de persistência com o banco de dados.

## Estratégias para Tratamento de Deleção envolvendo Associações

Entender como gerenciar a deleção de registros em um banco de dados relacional, especialmente quando há múltiplas
relações entre tabelas, é crucial para manter a integridade dos dados. Aqui estão algumas abordagens e boas práticas
para tratar essas situações.

### Tratamento de Deleção em Associações

1. **Cascade Delete (Deleção em Cascata)**
    - **Definição**: Permite que a deleção de um registro em uma tabela resulte automaticamente na deleção dos registros
      relacionados em outras tabelas.
    - **Uso**: Configurado em entidades JPA com a anotação `@OneToMany`, `@ManyToOne`, `@OneToOne`, ou `@ManyToMany` com
      a propriedade `cascade = CascadeType.REMOVE`.
    - **Cuidados**: Pode ser perigoso se não for usado corretamente, pois pode resultar na deleção de muitos registros
      relacionados inesperadamente.

   **Exemplo**:
   ```java
   @Entity
   public class Student {
       @Id
       private Long id;
       private String name;
   
       @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
       private List<Course> courses;
   }
   ```

2. **Restrict Delete (Restrição de Deleção)**
    - **Definição**: Impede a deleção de um registro se houver registros relacionados em outras tabelas.
    - **Uso**: Configurado no banco de dados com restrições de chave estrangeira (`FOREIGN KEY`) com a
      opção `ON DELETE RESTRICT`.
    - **Cuidados**: Assegura que não ocorra deleção de registros essenciais, mas requer tratamento de exceções na
      aplicação.

   **Exemplo SQL**:
   ```sql
   ALTER TABLE Course
   ADD CONSTRAINT fk_student
   FOREIGN KEY (student_id)
   REFERENCES Student(id)
   ON DELETE RESTRICT;
   ```

### Estratégias para Deleção Lógica

Quando a deleção física (remoção do registro da tabela) não é desejável devido a múltiplas relações ou necessidades de
auditoria/histórico, a deleção lógica é uma boa prática.

1. **Deleção Lógica com Campo de Status**
    - **Definição**: Em vez de deletar fisicamente o registro, um campo na tabela indica se o registro está ativo ou
      deletado.
    - **Uso**: Adiciona-se um campo como `isDeleted` ou `status` na entidade e filtra os registros baseados nesse campo.
    - **Cuidados**: Todas as consultas devem ser ajustadas para considerar o campo de deleção lógica.

   **Exemplo**:
   ```java
   @Entity
   public class Student {
       @Id
       private Long id;
       private String name;
       private boolean isDeleted; // Campo de deleção lógica
   }
   
   // Método para deletar logicamente um estudante
   public void deleteStudent(EntityManager entityManager, Long studentId) {
       Student student = entityManager.find(Student.class, studentId);
       if (student != null) {
           student.setDeleted(true);
           entityManager.merge(student);
       }
   }
   ```

2. **Estratégia de Versão (Soft Delete com Versões)**
    - **Definição**: Mantém o registro no banco de dados, mas cria uma nova versão quando há alterações ou deleções.
    - **Uso**: Usado em tabelas com um campo de versão e um campo de deleção lógica.
    - **Cuidados**: Pode aumentar a complexidade da consulta, mas é útil para auditoria e histórico de mudanças.

   **Exemplo**:
   ```java
   @Entity
   public class Student {
       @Id
       private Long id;
       private String name;
       private boolean isDeleted;
       private int version; // Campo de versão
   }
   
   // Método para deletar logicamente um estudante e incrementar a versão
   public void deleteStudent(EntityManager entityManager, Long studentId) {
       Student student = entityManager.find(Student.class, studentId);
       if (student != null) {
           student.setDeleted(true);
           student.setVersion(student.getVersion() + 1);
           entityManager.merge(student);
       }
   }
   ```

### Conclusão

A escolha entre deleção física e lógica depende do contexto da aplicação e dos requisitos específicos. Entender as
implicações de cada abordagem e aplicar as boas práticas adequadas ajuda a manter a integridade e a consistência dos
dados em sistemas complexos com múltiplas relações.
Criando um exemplo de aplicação Spring Boot que utiliza deleção lógica e controle de versão. A aplicação terá uma
entidade `Student` com campos de deleção lógica (`isDeleted`) e versão (`version`).

### 1. Dependências no `pom.xml`

Primeiro, adicione as dependências necessárias no seu `pom.xml`:

```xml

<dependencies>
    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- H2 Database for simplicity -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### 2. Configuração do Application.properties

Configure o `application.properties` para usar o banco de dados H2:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

### 3. Criação da Entidade `Student`

Crie a entidade `Student` com os campos `isDeleted` e `version`:

```java

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isDeleted = false; // Deleção lógica

    @Version
    private int version; // Controle de versão

    // Getters e Setters
}
```

### 4. Repositório `StudentRepository`

Crie o repositório `StudentRepository` para gerenciar a entidade `Student`:

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Custom query para buscar somente estudantes não deletados
    @Query("SELECT s FROM Student s WHERE s.isDeleted = false")
    List<Student> findAllNotDeleted();
}
```

### 5. Serviço `StudentService`

Crie um serviço para encapsular a lógica de negócios:

```java

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAllNotDeleted();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id).filter(student -> !student.isDeleted());
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        studentOpt.ifPresent(student -> {
            student.setDeleted(true);
            studentRepository.save(student);
        });
    }
}
```

### 6. Controlador `StudentController`

Crie um controlador para expor a API REST:

```java

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Optional<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
```

### 7. Classe Principal

Crie a classe principal para iniciar a aplicação:

```java
package br.com.uanderson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaHibernateApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaHibernateApplication.class, args);
    }
}
```

### 6. `entityManager.detach(entity)`

**Descrição:** O método `detach` é usado para desassociar uma entidade do contexto de persistência, fazendo com que ela
transite do estado "gerenciado" para o estado "desanexado" (detached). Uma entidade desanexada não será monitorada
pelo `EntityManager` para mudanças e, portanto, qualquer modificação feita após o `detach` não será sincronizada com o
banco de dados.

**Uso:**

```java
Student student = entityManager.find(Student.class, 1L);
entityManager.

detach(student);
```

**SQL Correspondente:** Nenhum SQL é gerado diretamente por esta operação.

**Explicação:** Quando você chama `detach`, a entidade é removida do contexto de persistência. Isso significa que
qualquer alteração feita na entidade após esse ponto não será gerenciada pelo `EntityManager` e não será sincronizada
com o banco de dados ao confirmar a transação.

### Exemplos de Comportamento

#### Comportamento somente com o `detach`:

```java
// Criar a fábrica de EntityManager (EntityManagerFactory)
try(EntityManagerFactory entityManagerFactory =
        new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                new MyPersistenceUnitInfo(), properties)){

        // Criar o EntityManager que gerencia o contexto de persistência
        try(
EntityManager entityManager = entityManagerFactory.createEntityManager()){

        // Iniciar uma transação
        entityManager.

getTransaction().

begin();

// Encontrar a entidade Student
Student student = entityManager.find(Student.class, 1L);

// Modificar o nome da entidade
        student.

setName("Uanderson");

// Desanexar a entidade do contexto de persistência
        entityManager.

detach(student);

// Confirmar a transação (salvar as alterações no banco de dados)
        entityManager.

getTransaction().

commit();

// A entidade student é impressa, mas a alteração não foi salva no banco de dados
        System.out.

println(student);
    }
            }
```

**Passos:**

1. O `entityManager.find(Student.class, 1L)` carrega a entidade `Student` com `id` 1 do banco de dados e a coloca no
   contexto de persistência.
2. A propriedade `name` do `student` é modificada para "Uanderson".
3. O `entityManager.detach(student)` desanexa a entidade `student` do contexto de persistência.
4. Quando a transação é confirmada (`commit`), a alteração do nome não é persistida no banco de dados porque a entidade
   foi desanexada antes da confirmação.

**SQL Correspondente:** Nenhum SQL é gerado, pois a entidade foi desanexada antes do `commit`.

#### Comportamento com `flush`:

```java
// Criar a fábrica de EntityManager (EntityManagerFactory)
try(EntityManagerFactory entityManagerFactory =
        new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                new MyPersistenceUnitInfo(), properties)){

        // Criar o EntityManager que gerencia o contexto de persistência
        try(
EntityManager entityManager = entityManagerFactory.createEntityManager()){

        // Iniciar uma transação
        entityManager.

getTransaction().

begin();

// Encontrar a entidade Student
Student student = entityManager.find(Student.class, 1L);

// Modificar o nome da entidade
        student.

setName("Uanderson");

// Sincronizar as alterações pendentes no banco de dados
        entityManager.

flush();

// Desanexar a entidade do contexto de persistência
        entityManager.

detach(student);

// Confirmar a transação (salvar as alterações no banco de dados)
        entityManager.

getTransaction().

commit();

// A entidade student é impressa, e a alteração foi salva no banco de dados
        System.out.

println(student);
    }
            }
```

**Passos:**

1. O `entityManager.find(Student.class, 1L)` carrega a entidade `Student` com `id` 1 do banco de dados e a coloca no
   contexto de persistência.
2. A propriedade `name` do `student` é modificada para "Uanderson".
3. O `entityManager.flush()` sincroniza as alterações pendentes no banco de dados. Isso força a execução de qualquer SQL
   necessário para refletir as mudanças feitas até o momento.
4. O `entityManager.detach(student)` desanexa a entidade `student` do contexto de persistência.
5. Quando a transação é confirmada (`commit`), a alteração do nome já foi persistida no banco de dados pelo `flush()`,
   então a confirmação não tem efeito adicional no banco de dados.

**SQL Correspondente:**

- O `entityManager.flush()` gera um `UPDATE` no banco de dados:
  ```sql
  UPDATE student SET name = 'Uanderson' WHERE id = 1;
  ```

### Resumo das Diferenças

**Primeiro Caso:** A entidade é desanexada antes do `commit`, portanto, nenhuma alteração é persistida no banco de
dados.

**Segundo Caso:** O `flush()` é chamado antes do `detach()`, forçando a sincronização das alterações pendentes com o
banco de dados. A modificação é persistida no banco de dados mesmo após o `detach()`.

### 7. `entityManager.clear()`

**Descrição:** O método `clear` é usado para desassociar todas as entidades do contexto de persistência, efetivamente
limpando o contexto.

**Uso:**

```java
entityManager.clear();
```

**SQL Correspondente:** Nenhum SQL é gerado para esta operação.

**Explicação:** `clear` remove todas as entidades do contexto de persistência, colocando-as no estado "detached".

### 8. `entityManager.flush()`

**Descrição:** O método `flush` é usado para sincronizar o estado atual do contexto de persistência com o banco de
dados.

**Uso:**

```java
entityManager.flush();
```

**SQL Correspondente:** Depende das operações pendentes. Por exemplo, se houver uma entidade a ser persistida:

```sql
INSERT INTO Student (name)
VALUES ('Uanderson Oliveira');
```

**Explicação:** Quando você chama `flush`, todas as mudanças pendentes no contexto de persistência são enviadas para o
banco de dados, mas a transação ainda não é comitada.

### `entityManager.contains(entity)`

**Descrição:** O método `contains` é usado para verificar se uma entidade está no contexto de persistência. Ele retorna `true` se a entidade estiver no contexto de persistência e `false` se não estiver.

**Uso:**

```java
Student student = entityManager.find(Student.class, 1L);
boolean isManaged = entityManager.contains(student);
```

**SQL Correspondente:** Nenhum SQL é gerado para esta operação.

### Exemplo de Comportamento

```java
// Criar a fábrica de EntityManager (EntityManagerFactory)
try (EntityManagerFactory entityManagerFactory =
             new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                     new MyPersistenceUnitInfo(), properties)) {

    // Criar o EntityManager que gerencia o contexto de persistência
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

        // Iniciar uma transação
        entityManager.getTransaction().begin();

        // Recuperar uma entidade no estado 'MANAGED'
        Student student = entityManager.find(Student.class, 1L);

        // Remover a entidade
        entityManager.remove(student);

        // Verificar se a entidade ainda está no contexto de persistência
        boolean isManaged = entityManager.contains(student);
        System.out.println(isManaged); // Esperado: false

        // Confirmar a transação (salvar as alterações no banco de dados)
        entityManager.getTransaction().commit();

        // Imprimir a entidade
        System.out.println(student);
    }
}
```

### Explicação:

1. **Recuperação da Entidade:**
    - A entidade `Student` é recuperada do banco de dados usando `entityManager.find(Student.class, 1L)`. Neste momento, a entidade está no estado gerenciado (`MANAGED`).

2. **Remoção da Entidade:**
    - A entidade `student` é removida do contexto de persistência com `entityManager.remove(student)`. Isso marca a entidade para remoção no banco de dados quando a transação for confirmada.

3. **Verificação com `contains`:**
    - O método `entityManager.contains(student)` é usado para verificar se a entidade `student` ainda está no contexto de persistência. Após a chamada para `remove`, a entidade não está mais no contexto de persistência, então `contains` retorna `false`.

4. **Confirmação da Transação:**
    - A transação é confirmada com `entityManager.getTransaction().commit()`, persistindo a remoção no banco de dados.

**Comportamento Detalhado:**
- **Antes da Remoção:**
    - `entityManager.find(Student.class, 1L)` carrega a entidade `Student` com ID 1 do banco de dados e a coloca no contexto de persistência. A entidade está no estado `MANAGED`.
    - `entityManager.contains(student)` retornaria `true` neste ponto, pois a entidade está no contexto de persistência.

- **Após a Remoção:**
    - `entityManager.remove(student)` desanexa a entidade `student` do contexto de persistência, marcando-a para remoção.
    - `entityManager.contains(student)` retorna `false` porque a entidade foi removida do contexto de persistência.
    - A confirmação da transação (`commit`) efetua a remoção no banco de dados.

### Considerações Importantes:
- **Estado da Entidade:** `contains` é útil para verificar se uma entidade está sendo gerenciada pelo `EntityManager` no momento.
- **Uso Comum:** É frequentemente usado para verificar se uma entidade desanexada precisa ser sincronizada novamente com o contexto de persistência usando métodos como `merge`.
- **Sem Geração de SQL:** O método `contains` opera apenas no contexto de persistência e não gera nenhum SQL.

### Resumo dos Métodos

- **`persist`**: Salva uma nova entidade no banco de dados.
    - **SQL:** `INSERT INTO Student (name) VALUES ('Uanderson Oliveira');`
- **`find`**: Busca uma entidade no banco de dados pelo ID.
    - **SQL:** `SELECT * FROM Student WHERE id = 1;`
- **`refresh`**: Sincroniza a entidade com o estado atual do banco de dados, descartando alterações não comitadas.
    - **SQL:** `SELECT * FROM Student WHERE id = 1;`
- **`merge`**: Atualiza uma entidade existente ou salva uma nova, retornando uma instância gerenciada.
    - **SQL:** `UPDATE Student SET name = 'Nome Atualizado' WHERE id = 1;`
- **`remove`**: Exclui uma entidade do banco de dados.
    - **SQL:** `DELETE FROM Student WHERE id = 1;`
- **`detach`**: Desassocia uma entidade do contexto de persistência.
    - **SQL:** Nenhum SQL gerado.
- **`clear`**: Desassocia todas as entidades do contexto de persistência.
    - **SQL:** Nenhum SQL gerado.
- **`flush`**: Sincroniza o contexto de persistência com o banco de dados sem comitar a transação.
    - **SQL:** Depende das operações pendentes.
- **`contains`**: Verifica se uma entidade está no contexto de persistência.
    - **SQL:** Nenhum SQL gerado.
  

Esses métodos são fundamentais para gerenciar o ciclo de vida das entidades em aplicações JPA/Hibernate, permitindo uma
interação eficiente e flexível com o banco de dados.




