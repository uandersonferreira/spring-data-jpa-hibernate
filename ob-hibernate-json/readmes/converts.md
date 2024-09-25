### Como Guardar Dados em JSON no Banco de Dados Usando Conversores em Spring Boot

Olá, eu sou Uanderson, um desenvolvedor backend Java em formação, e hoje vou te ajudar a entender como armazenar dados
no formato JSON em um banco de dados usando **Spring Boot** com **Hibernate**. Nosso foco será em como usar **convertes
** para facilitar esse processo, utilizando cenários do mundo real para contextualizar melhor.

> OLHE ESSA IMPLEMENTAÇÃO PODE AJUDAR TAMBÉM: https://github.com/uandersonferreira/springboot-essentials/blob/main/src/main/java/br/com/uanderson/springboot/wrapper/PageableResponse.java


### Cenário Real

Imagine que você tem uma aplicação onde o campo **detalhes** de um funcionário precisa ser flexível o suficiente para
armazenar diferentes tipos de informações adicionais, como preferências, notas, ou até mesmo configurações que não são
fixas e podem variar de funcionário para funcionário. A solução ideal seria armazenar essas informações em um campo do
tipo JSON, que pode ser facilmente serializado e deserializado.

#### Passo 1: Preparando o Projeto Spring Boot

Primeiro, você precisa configurar seu projeto Spring Boot. Adicione a dependência do Jackson para lidar com a
serialização e deserialização de JSON:

No arquivo `pom.xml` (para Maven), adicione:

```xml

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.2</version> <!-- Certifique-se de usar a versão mais atual -->
</dependency>
```

### Passo 2: Definindo a Entidade Employee

Aqui temos a nossa entidade **Employee**. Esse é o modelo que iremos usar para armazenar os dados no banco de dados.
Veja como adicionar um campo JSON usando um conversor:

```java

@Entity
@Table(name = "employees")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 30, nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;

    // Este campo JSON será mapeado para uma coluna de texto no banco de dados
    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> details; // Um mapa de dados flexíveis em formato JSON

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
```

### Passo 3: Criando o Conversor de JSON

Agora, precisamos de um **converter** para transformar o `Map<String, Object>` em uma string JSON quando salvar no banco
e vice-versa quando buscarmos os dados.

Aqui está o nosso `JsonConverter`:

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class JsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            // Converte o Map em uma String JSON para armazenar no banco de dados
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Se ocorrer algum erro na serialização
            throw new IllegalArgumentException("Erro ao converter o mapa para JSON: " + attribute, e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            // Converte a String JSON de volta para um Map
            return objectMapper.readValue(dbData, HashMap.class);
        } catch (IOException e) {
            // Se ocorrer algum erro na deserialização
            throw new IllegalArgumentException("Erro ao converter JSON para Map: " + dbData, e);
        }
    }
}
```

Esse **converter** usa o **Jackson** para converter o mapa em uma string JSON quando estamos salvando e reverter o
processo quando buscamos os dados.

### Passo 4: Salvando e Recuperando Dados

Agora que temos a entidade e o conversor prontos, podemos ver como salvar e recuperar dados JSON. Suponha que você tenha
um `EmployeeService` e um `EmployeeController` para gerenciar suas operações CRUD.

Aqui está um exemplo de serviço:

```java

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        // Exemplo de adição de detalhes personalizados em JSON
        Map<String, Object> details = new HashMap<>();
        details.put("hobby", "Programming");
        details.put("skill_level", "Junior");

        employee.setDetails(details);

        return employeeRepository.save(employee); // Salva no banco de dados
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}
```

### Passo 5: Testando a Aplicação

Aqui está um exemplo simples de como testar o salvamento de dados em JSON:

```java

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeService.getEmployee(id);
    }
}
```

Ao fazer uma solicitação `POST` para `/employees`, você pode enviar um novo funcionário com dados JSON em detalhes, e ao
recuperar o funcionário, você verá esses dados JSON.


___

### 1. **Exemplo de Conversão de JSON para Entidade e Vice-Versa: Configurações Dinâmicas de Usuário**

Imagine que você está desenvolvendo uma aplicação onde cada usuário pode ter diferentes preferências e configurações,
como tema, notificações, e visualizações. Como essas configurações podem variar muito, pode ser mais fácil armazená-las
em um campo JSON no banco de dados.

#### **Entidade:**

```java

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> preferences; // campo para armazenar as configurações dinâmicas

    // Getters e Setters
}
```

#### **JSON Converter:**

```java

@Converter
public class JsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Erro ao converter Map para JSON", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao converter JSON para Map", e);
        }
    }
}
```

#### **Como funciona na prática:**

Ao salvar um usuário com suas preferências, você pode simplesmente criar um `Map` com as configurações e o JPA irá
salvar como JSON no banco de dados. Ao buscar o usuário, o JPA automaticamente converte o JSON de volta para o `Map`.

```java
public User createUserWithPreferences() {
    User user = new User();
    user.setUsername("uanderson");

    Map<String, Object> preferences = new HashMap<>();
    preferences.put("theme", "dark");
    preferences.put("notifications", true);
    preferences.put("fontSize", 14);

    user.setPreferences(preferences);

    userRepository.save(user); // O Map será convertido para JSON e salvo no banco
    return user;
}
```

Quando você buscar o usuário do banco, o `Map` de preferências será convertido automaticamente para o formato JSON.

---

### 2. **Exemplo: Histórico de Ações do Usuário**

Outro caso comum é salvar o histórico de ações de um usuário em uma aplicação web. Pode ser útil armazenar essas ações
como JSON no banco de dados para que você possa recuperá-las e exibi-las facilmente no front-end.

#### **Entidade:**

```java

@Entity
@Table(name = "user_logs")
public class UserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> actions; // Lista de ações do usuário

    // Getters e Setters
}
```

#### **Inserindo Logs:**

```java
public void logUserAction(Long userId, String actionType, String description) {
    UserLog log = new UserLog();
    log.setUserId(userId);

    Map<String, Object> action = new HashMap<>();
    action.put("type", actionType);
    action.put("description", description);
    action.put("timestamp", LocalDateTime.now());

    List<Map<String, Object>> actions = new ArrayList<>();
    actions.add(action);

    log.setActions(actions);

    userLogRepository.save(log); // As ações são armazenadas como JSON
}
```

---

### 3. **Exemplo: Pedido com Itens Flexíveis**

Imagine um sistema de pedidos onde cada pedido pode ter itens com atributos variáveis. Por exemplo, um item pode ser um
produto físico com um SKU e quantidade, ou pode ser um serviço com uma descrição e duração.

#### **Entidade:**

```java

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> items; // Lista de itens variáveis no pedido

    // Getters e Setters
}
```

#### **Criando um Pedido com Itens Flexíveis:**

```java
public Order createOrder() {
    Order order = new Order();
    order.setCustomerName("João Silva");

    Map<String, Object> item1 = new HashMap<>();
    item1.put("type", "product");
    item1.put("sku", "123456");
    item1.put("quantity", 2);

    Map<String, Object> item2 = new HashMap<>();
    item2.put("type", "service");
    item2.put("description", "Consultoria de TI");
    item2.put("hours", 5);

    List<Map<String, Object>> items = new ArrayList<>();
    items.add(item1);
    items.add(item2);

    order.setItems(items);

    orderRepository.save(order); // Itens são salvos como JSON no banco
    return order;
}
```

Nesse cenário, você pode salvar diferentes tipos de itens (produtos, serviços, etc.) em uma estrutura flexível no banco
de dados usando JSON, o que facilita a extensão do sistema para novos tipos de itens sem alterar a estrutura da tabela.

---

### Vantagens do Uso de JSON com Conversores:

1. **Flexibilidade**: Permite armazenar dados variáveis ou não estruturados sem a necessidade de alterar a estrutura do
   banco.
2. **Simplicidade**: Com um `Converter`, o JPA lida automaticamente com a conversão de JSON para entidades Java e
   vice-versa.
3. **Escalabilidade**: Você pode adaptar facilmente as estruturas de dados sem modificar a base de dados, como adicionar
   novas propriedades em um mapa JSON.

### Exemplo de JSON Complexo

Vamos começar com um JSON mais complexo que pode ser recebido em uma API:

```json
{
  "user_id": 12345,
  "name": "Alice",
  "address": {
    "street": "123 Main St",
    "city": "Wonderland",
    "zipcode": "12345"
  },
  "roles": [
    "Admin",
    "User"
  ],
  "metadata": {
    "signup_date": "2023-09-25",
    "last_login": "2023-09-30"
  }
}
```

Neste JSON, temos:

- Um campo simples `user_id`
- Um objeto aninhado `address`
- Uma lista `roles`
- Um objeto `metadata` com campos de datas

### Passo 1: Definir as Entidades para o Mapeamento

Você precisará definir várias classes para representar essa estrutura complexa. Vamos usar a anotação `@JsonProperty`
para garantir que o mapeamento dos campos funcione corretamente, mesmo que os nomes das variáveis Java sejam diferentes
das chaves JSON.

#### Classe `User`

```java
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class User {

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    @JsonProperty("address")
    private Address address; // Um objeto aninhado

    private List<String> roles; // Uma lista de strings

    @JsonProperty("metadata")
    private Metadata metadata; // Outro objeto aninhado

    // Getters e Setters
}
```

#### Classe `Address`

```java
public class Address {

    private String street;
    private String city;

    @JsonProperty("zipcode")
    private String zipCode;

    // Getters e Setters
}
```

#### Classe `Metadata`

```java
import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {

    @JsonProperty("signup_date")
    private String signupDate;

    @JsonProperty("last_login")
    private String lastLogin;

    // Getters e Setters
}
```

### Passo 2: Configurar o `ObjectMapper` e Lidar com JSON Complexo

No Spring Boot, o `ObjectMapper` do Jackson faz automaticamente a desserialização de JSON para essas classes, desde que
você forneça as anotações corretas.

Aqui está como você pode receber esse JSON em uma API e convertê-lo para a entidade `User`:

#### Controller com Spring Boot

```java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Aqui o Spring Boot já mapeou o JSON complexo para a entidade User
        System.out.println("User ID: " + user.getUserId());
        System.out.println("User Name: " + user.getName());
        System.out.println("User Address: " + user.getAddress().getStreet());
        System.out.println("Roles: " + user.getRoles());
        System.out.println("Metadata Signup Date: " + user.getMetadata().getSignupDate());

        // Processa a entidade (ex.: salvar no banco)
        return ResponseEntity.ok(user);
    }
}
```

### Passo 3: Explicação do Mapeamento

Neste exemplo:

1. **Campos Simples**: O campo `user_id` do JSON é mapeado diretamente para `userId` na entidade `User` usando a
   anotação `@JsonProperty`.

2. **Objetos Aninhados**: O objeto `address` no JSON é mapeado para a classe `Address`. Isso permite que o Spring Boot
   trate automaticamente esse objeto aninhado e crie a instância da classe `Address` com base no JSON.

3. **Listas**: A lista `roles` no JSON é mapeada para uma lista de Strings (`List<String>`) na entidade `User`. Isso
   mostra como tratar arrays ou listas no JSON sem esforço extra.

4. **Metadados**: O objeto `metadata` no JSON é mapeado para a classe `Metadata`, o que permite uma organização limpa
   dos dados dentro da entidade.

### Passo 4: Estruturas Mais Dinâmicas com `Map<String, Object>`

Em cenários onde o JSON é ainda mais dinâmico (com campos que podem variar), você pode usar um `Map<String, Object>`para
lidar com dados que mudam de forma ou que não são conhecidos previamente.

#### Modificando a Entidade `User` para Lidar com Metadados Dinâmicos

```java
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.Map;
import java.util.HashMap;

public class User {

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    @JsonProperty("address")
    private Address address;

    private List<String> roles;

    // Usando um Map para campos dinâmicos em 'metadata'
    private Map<String, Object> metadata = new HashMap<>();

    @JsonAnySetter
    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    // Getters e Setters
}
```

Agora, qualquer campo extra que seja passado dentro de `metadata` no JSON será adicionado ao `Map<String, Object>`.

### Exemplo de JSON Dinâmico

Se o JSON tiver campos adicionais dentro de `metadata`, como:

```json
{
  "user_id": 12345,
  "name": "Alice",
  "address": {
    "street": "123 Main St",
    "city": "Wonderland",
    "zipcode": "12345"
  },
  "roles": [
    "Admin",
    "User"
  ],
  "metadata": {
    "signup_date": "2023-09-25",
    "last_login": "2023-09-30",
    "favorite_color": "Blue",
    "last_purchase_amount": 120.50
  }
}
```

Esses campos extras serão adicionados ao `Map<String, Object>` automaticamente, sem a necessidade de criar novos
atributos na classe `Metadata`.

### Conclusão

Este exemplo expandido mostra como lidar com **estruturas JSON complexas** em aplicações Java/Spring Boot, desde o
mapeamento de objetos aninhados, listas e campos simples, até a manipulação de metadados dinâmicos usando
`Map<String, Object>`.

Essa técnica é útil em cenários reais, como quando você consome APIs externas com estrutura de dados variáveis ou quando
precisa lidar com dados opcionais ou inesperados que são retornados no JSON.
