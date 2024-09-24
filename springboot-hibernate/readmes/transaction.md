Olá, eu sou o Uanderson, um desenvolvedor Backend Java em formação, e hoje eu vou te guiar em um assunto fundamental
para qualquer aplicação Java robusta: **implementação de transações em operações CRUD (Create, Read, Update, Delete)**
usando **Spring Boot** com **Hibernate** e **JPA**, além de um exemplo com **Hibernate puro**.

Se você é um desenvolvedor júnior e nunca trabalhou diretamente com transações antes, não se preocupe. Vamos abordar
isso de forma simples, passo a passo. E, ao final, você vai entender como garantir que as operações de banco de dados
aconteçam de forma atômica (tudo ou nada), o que é essencial para a integridade dos dados da sua aplicação.

### **O Que São Transações?**

Pensa nas transações como um contrato entre sua aplicação e o banco de dados. Quando você faz várias operações em
sequência (por exemplo, criar um usuário, adicionar um endereço, fazer um pedido), você quer garantir que **todas as
operações sejam executadas com sucesso ou nenhuma delas seja salva**. Se algo der errado no meio, o sistema "volta
atrás" e nada é modificado.

### **Por Que Isso Importa?**

Imagine que você está fazendo uma compra online. Você adiciona um produto ao carrinho, mas, no meio do processo de
pagamento, acontece uma falha. O que deveria acontecer? **Nada deve ser registrado no banco de dados**, certo? Você não
quer que seu saldo seja debitado sem que o pedido seja registrado corretamente. É para evitar esse tipo de problema que
as transações existem.

Agora, vamos ver como implementar isso na prática, usando tanto **JPA** (a forma mais comum e "automática" no Spring
Boot) quanto **Hibernate puro** (que exige um controle mais manual).

### **1. Transações Usando Spring Boot + JPA**

O Spring Boot já facilita muito nossa vida quando se trata de transações. Basicamente, tudo o que você precisa fazer é
adicionar a anotação `@Transactional` nos métodos que realizam operações de banco de dados. Essa anotação faz com que o
Spring cuide de iniciar e finalizar a transação para você.

#### **Passo 1: Configuração de Dependências**

No arquivo `pom.xml`, certifique-se de ter as seguintes dependências:

```xml

<dependencies>
    <!-- Dependências do Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Driver do banco de dados H2 para testes locais -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Hibernate Core -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
    </dependency>
</dependencies>
```

#### **Passo 2: Criando a Entidade**

Aqui vamos criar uma entidade simples, `Employee`, que representa um funcionário em nossa aplicação:

```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Getters e Setters
    // Construtores
}
```

#### **Passo 3: Implementando o CRUD com Transações**

Agora, vamos implementar os métodos CRUD (criação, leitura, atualização, exclusão) no nosso repositório e garantir que
todas essas operações sejam feitas dentro de uma transação.

```java
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // CREATE
    @Transactional
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // READ
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // UPDATE
    @Transactional
    public Employee updateEmployee(Long id, String name, String email) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setName(name);
        employee.setEmail(email);
        return employeeRepository.save(employee);
    }

    // DELETE
    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
```

### **Como Funciona:**

- A anotação `@Transactional` faz com que cada método seja executado dentro de uma transação.
    - **`@Transactional(readOnly = true)`**: Utilizado para operações de leitura (sem modificação no banco).
    - **Sem `readOnly`**: Utilizado para operações que envolvem modificação de dados.
- Se houver uma exceção no meio da execução do método, a transação é **revertida** (nenhuma alteração é salva no banco).

### **2. Implementando Transações com Hibernate Puro**

Agora, vamos ver como fazer isso com **Hibernate puro**. Aqui, temos um controle mais manual das transações.

#### **Passo 1: Configuração do Hibernate**

No arquivo de configuração `hibernate.cfg.xml`, conectamos o Hibernate ao banco de dados:

```xml

<hibernate-configuration>
    <session-factory>
        <property name="hibernate.dialect">org.hibernate.dialect.H2Dialect</property>
        <property name="hibernate.hbm2ddl.auto">update</property>
        <property name="hibernate.show_sql">true</property>
        <property name="hibernate.format_sql">true</property>

        <property name="hibernate.connection.driver_class">org.h2.Driver</property>
        <property name="hibernate.connection.url">jdbc:h2:mem:testdb</property>
        <property name="hibernate.connection.username">sa</property>
        <property name="hibernate.connection.password"></property>
    </session-factory>
</hibernate-configuration>
```

#### **Passo 2: Operações CRUD com Transações Manuais**

Aqui, vamos implementar as mesmas operações CRUD, mas agora controlando as transações manualmente:

```java
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeDAO {

    private Session session;

    public EmployeeDAO(Session session) {
        this.session = session;
    }

    // CREATE
    public Employee createEmployee(Employee employee) {
        Transaction transaction = session.beginTransaction();
        try {
            session.save(employee);
            transaction.commit(); // Confirma as mudanças no banco
        } catch (Exception e) {
            transaction.rollback(); // Reverte as mudanças se ocorrer erro
            throw e;
        }
        return employee;
    }

    // READ
    public List<Employee> getAllEmployees() {
        return session.createQuery("from Employee", Employee.class).list();
    }

    // UPDATE
    public Employee updateEmployee(Long id, String name, String email) {
        Transaction transaction = session.beginTransaction();
        try {
            Employee employee = session.get(Employee.class, id);
            if (employee == null) throw new RuntimeException("Employee not found");

            employee.setName(name);
            employee.setEmail(email);
            session.update(employee);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
        return employee;
    }

    // DELETE
    public void deleteEmployee(Long id) {
        Transaction transaction = session.beginTransaction();
        try {
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
                transaction.commit();
            }
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
```

### **O Que Acontece Aqui:**

- O `Transaction transaction = session.beginTransaction();` inicia a transação.
- A transação é **comitada** com `transaction.commit()` se tudo der certo, ou **revertida** com `transaction.rollback()`
  se houver um erro.
- Esse é o processo manual de controle de transações usando Hibernate puro.

### **Resumo Geral**

Agora você entende como controlar transações em operações CRUD tanto com **Spring Boot + JPA** quanto com **Hibernate
puro**. Se você usar Spring, o controle de transações é bem mais simples com a anotação `@Transactional`, que lida com a
maior parte do trabalho para você. No entanto, é importante também saber como controlar transações manualmente, pois em
algumas situações mais avançadas ou específicas isso pode ser necessário.

Espero que você tenha gostado do tutorial e, mais importante, que agora entenda como **garantir a integridade dos dados
** na sua aplicação.

### Exemplo de implementação:

Dessa forma o empregado não é inserido se o method estiver assim: 

```java
@Override
public Employee save(Employee employee) {
    try {
        //session.beginTransaction();

        session.persist(employee);

        //session.getTransaction().commit();

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return employee;
}
```

O motivo pelo qual o empregado não é inserido quando o método está implementado dessa forma é que a transação **não está sendo gerenciada corretamente**. Vou explicar em detalhes:

### 1. **Falta de Controle de Transação**

O código que você apresentou está chamando o método `session.persist(employee)`, que é responsável por inserir o objeto `Employee` no banco de dados. No entanto, essa operação precisa ocorrer dentro de uma **transação ativa**. O trecho de código que deveria gerenciar a transação (iniciá-la e confirmá-la) está comentado:

```java
//session.beginTransaction();
//session.getTransaction().commit();
```

Sem esses dois comandos, o Hibernate **não sabe quando deve enviar as alterações** para o banco de dados. Mesmo que você chame `persist()`, o Hibernate só armazena as mudanças no seu "contexto de persistência" até que uma transação seja iniciada e confirmada.

- **session.beginTransaction()**: Inicia a transação, informando ao Hibernate que as operações seguintes devem ser tratadas como parte de uma transação.
- **session.getTransaction().commit()**: Finaliza a transação, confirmando as operações no banco de dados. É somente nesse momento que o Hibernate envia os dados para serem inseridos fisicamente no banco.

### 2. **O Papel da Transação**

Transações garantem que uma série de operações sejam executadas como uma unidade atômica, ou seja, ou todas ocorrem com sucesso, ou nenhuma delas acontece. No Hibernate, sem uma transação ativa, o `persist()` apenas prepara o objeto para ser inserido, mas a inserção **não acontece efetivamente no banco de dados até que a transação seja "commitada"**.

### 3. **O Que Está Acontecendo no Seu Código**

- Você chama `session.persist(employee)` para persistir o objeto `Employee`, o que funciona no nível do Hibernate. No entanto, como não há uma transação ativa, o Hibernate não envia o comando SQL `INSERT` para o banco de dados imediatamente.
- Como o trecho de código responsável por iniciar e finalizar a transação está comentado, o Hibernate não sabe que deve aplicar as alterações ao banco.
- No final, o objeto `Employee` não é realmente inserido no banco de dados porque o comando `commit()` (que confirmaria a transação) nunca é chamado.

### **Como Corrigir?**

A solução é garantir que o método **inicie uma transação e a finalize corretamente**. Aqui está o código corrigido:

```java
@Override
public Employee save(Employee employee) {
    try {
        // Inicia a transação
        session.beginTransaction();

        // Persiste o objeto Employee
        session.persist(employee);

        // Confirma (commita) a transação, inserindo o empregado no banco
        session.getTransaction().commit();

    } catch (Exception ex) {
        // Se ocorrer um erro, desfaz a transação
        session.getTransaction().rollback();
        ex.printStackTrace();
    }

    return employee;
}
```

### **Explicação do Código Corrigido**:

1. **session.beginTransaction()**: Inicia uma nova transação. Isso indica ao Hibernate que qualquer operação que se seguir deve ser tratada como parte dessa transação.
2. **session.persist(employee)**: Persiste o objeto `Employee` no contexto do Hibernate.
3. **session.getTransaction().commit()**: Confirma a transação, ou seja, o Hibernate envia o comando SQL `INSERT` ao banco de dados e as mudanças são aplicadas.
4. **session.getTransaction().rollback()**: Se ocorrer uma exceção, o rollback garante que as alterações não aplicadas sejam desfeitas, mantendo a integridade dos dados.

### **Conclusão**

Sem a transação ativa, o Hibernate não faz o commit das mudanças no banco de dados, resultando na ausência da inserção do `Employee`. As operações CRUD precisam de uma transação explícita para garantir que as alterações sejam salvas de fato.