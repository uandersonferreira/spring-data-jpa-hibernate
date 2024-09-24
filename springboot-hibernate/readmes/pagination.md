Olá, eu sou o Uanderson, um desenvolvedor backend Java em formação, e estou aqui para guiar você, desenvolvedor júnior,
nesse processo. Hoje, vamos falar sobre como implementar **paginação** em uma aplicação **Spring Boot** utilizando o *
*Hibernate**. Isso é essencial quando você tem um banco de dados grande e não quer carregar todos os registros de uma
vez, mas sim dividi-los em partes menores, ou "páginas", para otimizar o desempenho da sua aplicação.

Imagine que você está lendo um livro muito extenso, mas ele não tem páginas numeradas. Isso tornaria bem complicado
encontrar onde você parou ou acessar uma parte específica rapidamente, certo? A mesma coisa acontece em sistemas: se
você tentar carregar todos os dados de uma só vez, pode acabar sobrecarregando o sistema. É aí que entra a **paginação
**!

### 1. **Entendendo a Paginação**

Paginação basicamente divide grandes conjuntos de dados em partes menores. Por exemplo, se você tiver uma tabela de
10.000 registros, é mais eficiente mostrar 20 ou 50 registros por vez. Isso reduz o tempo de carregamento, melhora a
experiência do usuário e evita a sobrecarga da aplicação.

Aqui, vamos ver como você pode implementar essa funcionalidade em uma aplicação Spring Boot usando Hibernate.

### 2. **Preparando o Ambiente**

Antes de mais nada, certifique-se de que você tenha o seguinte:

- **Spring Boot** configurado.
- **Hibernate** configurado como o provedor JPA.
- Um banco de dados configurado (vamos usar PostgreSQL neste exemplo).

Se ainda não tem o projeto pronto, crie um novo projeto Spring Boot com o **Spring Initializr
** ([https://start.spring.io/](https://start.spring.io/)) e adicione as dependências **Spring Data JPA** e **PostgreSQL
Driver**.

### 3. **Configurando o Banco de Dados**

No arquivo `application.properties` ou `application.yml`, configure a conexão com o banco de dados:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

Essas propriedades são autoexplicativas. Elas definem a conexão com o banco de dados e pedem para o Hibernate atualizar
o schema do banco, se necessário.

### 4. **Criando a Entidade Employee**

Agora, vamos criar uma entidade que representa um funcionário. Aqui, o `Employee` tem um `id`, um `name` e um `email`.

```java

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

Esse código mapeia a tabela `employees` para a entidade `Employee`. O `@Id` indica que o campo `id` é a chave primária e
o `@GeneratedValue` faz o Hibernate gerar o valor automaticamente.

### 5. **Criando o Repositório**

O próximo passo é criar um repositório que faça consultas no banco de dados. Vamos usar o `JpaRepository`, que já nos dá
métodos prontos para buscar e paginar dados.

```java

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
```

O `JpaRepository` nos oferece métodos como `findAll(Pageable pageable)`, que facilita a implementação da paginação.

### 6. **Implementando o Serviço com Paginação**

Agora, vamos criar um serviço que implementa a lógica da paginação. O serviço vai consultar o repositório e devolver uma
página específica de funcionários.

```java

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Page<Employee> getEmployeesWithPagination(int page, int size) {
        // Pageable: objeto que define qual página e quantos registros por página
        Pageable pageable = PageRequest.of(page, size);

        // Busca os funcionários paginados
        return employeeRepository.findAll(pageable);
    }
}
```

Neste código, o método `getEmployeesWithPagination` usa o `PageRequest.of(page, size)` para criar um objeto `Pageable`
que define a página e o tamanho da página. Em seguida, ele usa o método `findAll(pageable)` do repositório para buscar
os funcionários de forma paginada.

### 7. **Expondo o Endpoint de Paginação**

Vamos criar um controlador para expor um endpoint que permita ao cliente solicitar uma página específica de
funcionários.

```java

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public Page<Employee> getEmployees(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        // Chama o serviço para retornar a página desejada
        return employeeService.getEmployeesWithPagination(page, size);
    }
}
```

Aqui, o método `getEmployees` recebe os parâmetros `page` e `size` na URL (por exemplo, `/employees?page=0&size=10`).
Esses parâmetros são usados para definir qual página e quantos registros exibir por página. O valor padrão da página é 0
e o tamanho da página é 10, caso o cliente não informe esses valores.

### 8. **Testando a Paginação**

Para testar, basta executar a aplicação e acessar a URL:

```
http://localhost:8080/employees?page=1&size=5
```

Isso vai retornar os funcionários da página 1, com 5 registros por página.


___

Agora vamos criar uma **implementação de paginação usando Hibernate puro**, sem utilizar as facilidades do
`JpaRepository` ou outras abstrações prontas. Vou mostrar como você pode implementar sua própria lógica de paginação diretamente com consultas Hibernate.

Vamos focar em dois exemplos:

1. **Usando HQL (Hibernate Query Language)**: A sintaxe do Hibernate que é muito similar ao SQL, mas trabalha com as
   entidades em vez de tabelas do banco de dados.
2. **Criando um método customizado de paginação**: Vamos implementar uma lógica que calcula o total de registros e
   divide em páginas manualmente.

### 1. **Configuração Básica do Hibernate**

Primeiro, precisamos garantir que o Hibernate esteja corretamente configurado para trabalhar com o banco de dados.
Supondo que sua configuração do Hibernate já esteja funcionando, o foco será a criação do código de paginação.

### 2. **Criando o Método de Paginação com Hibernate Puro**

Aqui, faremos o seguinte:

- Vamos calcular o número total de registros.
- Definir o tamanho de cada página (quantidade de registros por página).
- Buscar a página correta com base nesses parâmetros.

#### **Passo a Passo: Paginação Usando HQL**

```java
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class EmployeeDAO {

    private Session session;

    public EmployeeDAO(Session session) {
        this.session = session;
    }

    // Método para retornar os registros paginados
    public List<Employee> findEmployeesWithPagination(int page, int pageSize) {
        // Calcula o deslocamento (offset) para saber de onde começar a buscar
        int offset = (page - 1) * pageSize;

        // Cria a consulta HQL, que busca todos os registros da entidade Employee
        Query<Employee> query = session.createQuery("from Employee", Employee.class);

        // Define o número de registros por página
        query.setMaxResults(pageSize);

        // Define o ponto de partida dos registros (página atual)
        query.setFirstResult(offset);

        // Executa a consulta e retorna a lista de funcionários
        return query.list();
    }

    // Método para contar o número total de registros
    public Long countEmployees() {
        // Consulta para contar o total de registros
        String countHQL = "SELECT count(e.id) FROM Employee e";
        Query<Long> query = session.createQuery(countHQL, Long.class);

        // Executa a consulta e retorna o total de funcionários
        return query.uniqueResult();
    }
}
```

### **Explicação do Código**

- **`findEmployeesWithPagination`**: Esse método realiza a consulta paginada. Ele recebe dois parâmetros: `page` (qual
  página você deseja) e `pageSize` (quantos registros por página). Usamos o `setFirstResult` para definir o ponto de
  partida da página e o `setMaxResults` para definir o número de registros que serão retornados.

- **`countEmployees`**: Esse método simplesmente conta o número total de funcionários no banco de dados, o que pode ser
  útil para calcular quantas páginas são necessárias.

### **Calculando o Número de Páginas**

Se você quiser também calcular o número total de páginas com base no número total de registros e o tamanho da página,
faça algo assim:

```java
public int getTotalPages(int pageSize) {
    // Conta o total de funcionários
    Long totalEmployees = countEmployees();

    // Calcula o número total de páginas
    return (int) Math.ceil((double) totalEmployees / pageSize);
}
```

Isso vai pegar o número total de registros (`totalEmployees`) e dividir pelo tamanho da página (`pageSize`). O método
`Math.ceil` é usado para arredondar para cima, garantindo que, mesmo que sobre apenas um registro, ele será exibido em
uma página extra.

### 3. **Exemplo Completo: Controller e Serviço**

Agora que temos o DAO configurado, podemos integrá-lo em um **serviço** e **controlador** para expor a paginação via
API.

#### **Serviço: EmployeeService**

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public List<Employee> getEmployeesPaginated(int page, int pageSize) {
        return employeeDAO.findEmployeesWithPagination(page, pageSize);
    }

    public int getTotalPages(int pageSize) {
        return employeeDAO.getTotalPages(pageSize);
    }
}
```

#### **Controlador: EmployeeController**

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        // Retorna a lista de funcionários paginados
        return employeeService.getEmployeesPaginated(page, pageSize);
    }

    @GetMapping("/employees/pages")
    public int getTotalPages(@RequestParam(defaultValue = "10") int pageSize) {
        // Retorna o número total de páginas
        return employeeService.getTotalPages(pageSize);
    }
}
```

### **Explicação**

1. **`EmployeeService`**: A camada de serviço encapsula a lógica de negócios. Aqui estamos chamando o `EmployeeDAO` para
   buscar os funcionários paginados e calcular o número de páginas.

2. **`EmployeeController`**: O controlador recebe as requisições HTTP e delega as chamadas ao serviço. O endpoint
   `/employees` retorna a lista de funcionários paginados, enquanto o `/employees/pages` retorna o número total de
   páginas.

### 4. **Testando a Aplicação**

Agora que tudo está configurado, você pode testar a paginação acessando os seguintes endpoints:

- Para buscar a primeira página com 10 registros:
  ```
  http://localhost:8080/employees?page=1&pageSize=10
  ```

- Para saber o número total de páginas:
  ```
  http://localhost:8080/employees/pages?pageSize=10
  ```

### Conclusão

Neste exemplo, implementamos nossa própria lógica de paginação usando **Hibernate puro**, sem utilizar as abstrações
prontas do Spring Data. Isso dá a você mais controle sobre o processo, o que pode ser útil quando você quer otimizar ou
personalizar a maneira como os dados são paginados.

Esse tipo de implementação pode ser necessário quando você trabalha com grandes volumes de dados e quer garantir que a
aplicação permaneça rápida e eficiente.
