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

**Descrição:** O método `merge` é usado para atualizar uma entidade existente no banco de dados ou para salvar uma nova
entidade, caso ela ainda não exista. Ele retorna uma instância gerenciada da entidade.

**Uso:**

```java
Student detachedStudent = new Student();
detachedStudent.

setId(1L);
detachedStudent.

setName("Nome Atualizado");

Student managedStudent = entityManager.merge(detachedStudent);
```

**SQL Correspondente:**

```sql
UPDATE Student
SET name = 'Nome Atualizado'
WHERE id = 1;
```

**Explicação:** `merge` é usado quando você tem uma entidade que não está atualmente gerenciada pelo contexto de
persistência (detached) e você quer aplicar as mudanças feitas a ela no banco de dados. Ele retorna uma nova instância
gerenciada da entidade.

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

Entender como gerenciar a deleção de registros em um banco de dados relacional, especialmente quando há múltiplas relações entre tabelas, é crucial para manter a integridade dos dados. Aqui estão algumas abordagens e boas práticas para tratar essas situações.

### Tratamento de Deleção em Associações

1. **Cascade Delete (Deleção em Cascata)**
    - **Definição**: Permite que a deleção de um registro em uma tabela resulte automaticamente na deleção dos registros relacionados em outras tabelas.
    - **Uso**: Configurado em entidades JPA com a anotação `@OneToMany`, `@ManyToOne`, `@OneToOne`, ou `@ManyToMany` com a propriedade `cascade = CascadeType.REMOVE`.
    - **Cuidados**: Pode ser perigoso se não for usado corretamente, pois pode resultar na deleção de muitos registros relacionados inesperadamente.

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
    - **Uso**: Configurado no banco de dados com restrições de chave estrangeira (`FOREIGN KEY`) com a opção `ON DELETE RESTRICT`.
    - **Cuidados**: Assegura que não ocorra deleção de registros essenciais, mas requer tratamento de exceções na aplicação.

   **Exemplo SQL**:
   ```sql
   ALTER TABLE Course
   ADD CONSTRAINT fk_student
   FOREIGN KEY (student_id)
   REFERENCES Student(id)
   ON DELETE RESTRICT;
   ```

### Estratégias para Deleção Lógica

Quando a deleção física (remoção do registro da tabela) não é desejável devido a múltiplas relações ou necessidades de auditoria/histórico, a deleção lógica é uma boa prática.

1. **Deleção Lógica com Campo de Status**
    - **Definição**: Em vez de deletar fisicamente o registro, um campo na tabela indica se o registro está ativo ou deletado.
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

A escolha entre deleção física e lógica depende do contexto da aplicação e dos requisitos específicos. Entender as implicações de cada abordagem e aplicar as boas práticas adequadas ajuda a manter a integridade e a consistência dos dados em sistemas complexos com múltiplas relações.
Criando um exemplo de aplicação Spring Boot que utiliza deleção lógica e controle de versão. A aplicação terá uma entidade `Student` com campos de deleção lógica (`isDeleted`) e versão (`version`).

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
transite do estado "gerenciado" para o estado "detached".

**Uso:**

```java
Student student = entityManager.find(Student.class, 1L);
entityManager.

detach(student);
```

**SQL Correspondente:** Nenhum SQL é gerado para esta operação.

**Explicação:** Quando você chama `detach`, a entidade é removida do contexto de persistência e quaisquer mudanças
feitas a ela após esse ponto não serão sincronizadas com o banco de dados.

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

Esses métodos são fundamentais para gerenciar o ciclo de vida das entidades em aplicações JPA/Hibernate, permitindo uma
interação eficiente e flexível com o banco de dados.




