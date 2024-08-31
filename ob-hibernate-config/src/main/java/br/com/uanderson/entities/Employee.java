package br.com.uanderson.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Representa uma tabela na base de dados
 */
@Entity
@Table(name = "ob_employees") //Optional
@NamedQuery(name = "Employee.mostPaid", query = "from Employee e where e.salary > 50000")
public class Employee implements Serializable {

    //Attributes (cada atributo representa uma coluna na tabela do banco de dados)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", length = 30, nullable = false)
    //Por default usa o nome do atributo com camelCase e não em snake_case
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true)
    private String email;
    private Integer age;
    private Double salary;
    private Boolean married;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "register_date")
    private LocalDateTime registerDate;
    @Column(name = "edit_date")
    private LocalDateTime editDate;
    /**
     * Indica que `nicknames` é uma coleção de elementos básicos. A anotação @ElementCollection
     * é usada para mapear coleções de tipos básicos ou embutidos.
     *
     * @ElementCollection A anotação @ElementCollection é usada no JPA (Java Persistence API) para mapear uma
     * coleção de tipos básicos ou classes incorporáveis (embeddable) em uma entidade.
     * Isso é útil quando você tem uma coleção de valores simples, como Strings ou Integers, ou
     * uma coleção de objetos embutidos que não têm uma identidade independente, ou seja,
     * eles não são entidades por si só.
     * @CollectionTable Define a tabela de coleção que armazenará os elementos da coleção.
     * - `name` : Nome da tabela onde os elementos da coleção serão armazenados. No caso, "employee_nicknames".
     * - `joinColumns` : Especifica a coluna de junção que faz referência à chave primária da entidade `Employee`.
     * No caso, a coluna `employee_id` na tabela `employee_nicknames` será usada como chave estrangeira para associar
     * os apelidos ao funcionário correspondente.
     * @Column Define o nome da coluna que armazenará os valores da coleção. No caso, "nickname".
     * <p>
     * Estrutura da tabela gerada:
     * ```
     * CREATE TABLE employee_nicknames (
     * employee_id BIGINT NOT NULL,        // Chave estrangeira referenciando a chave primária da tabela ob_employees
     * nickname VARCHAR(255),              // Coluna para armazenar cada apelido do empregado
     * PRIMARY KEY (employee_id, nickname), // Chave primária composta
     * FOREIGN KEY (employee_id) REFERENCES ob_employees(id) // Define employee_id como chave estrangeira
     * );
     * ```
     */
    @ElementCollection
    @CollectionTable(name = "employee_nicknames", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "nickname")
    private List<String> nickNames = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "employee_postalcode", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "postal_code")
    private List<Integer> postalCode = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "employee_creditcards", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "credit_cards")
    private Set<String> creditCards = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "employee_phones", joinColumns = @JoinColumn(name = "employee_id"))
    @MapKeyColumn(name = "phone_key") // Nome da coluna que armazena as chaves do mapa (números de telefone)
    @Column(name = "phone_value") // Nome da coluna que armazena os valores do mapa (companhias telefônicas)
    private Map<String, String> phones = new HashMap<>();

    @Enumerated(EnumType.STRING)//Por default EnumType value is ORDINAL. (0,1,2,3..))
    private EmployeeCategory category;

    // ======================= ASSOCIAÇÃO: ONE TO ONE ===============================
    //1. Associação com chave estrangeira: cria-se uma nova coluna na tabela
    @OneToOne //Um Employee para uma Direction 1...1
    //@OneToOne(cascade = CascadeType.ALL) //As operações são persistidas em suas associações
    @JoinColumn(name = "direction_pk", foreignKey = @ForeignKey(name = "fk_employee_direction"))
    //permite add o nome da coluna

    //2. Associação com tabela
    //@OneToOne(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "ob_employee_direction", //Nome da nova tabela que irá ter as PK das entidades
//            joinColumns = @JoinColumn(name = "employee_id"), //nome da col da entidade que mantem a relação
//            inverseJoinColumns = @JoinColumn(name = "direction_id") // nome da col da outra entidade da
//    )

    //3. forma
    //@OneToOne
    //@PrimaryKeyJoinColumn

    //4. forma
    // @OneToOne
    //@MapsId
    private Direction direction;

    // ======================= ASSOCIAÇÃO: ONE TO MANY ===============================
    @OneToMany
    @JoinTable(
            name = "ob_employee_cars", //Nome da nova tabela que irá ter as PK das entidades
            joinColumns = @JoinColumn(name = "employee_id"), //nome da col da entidade que mantem a relação
            inverseJoinColumns = @JoinColumn(name = "car_id") // nome da col da outra entidade da
    )
    private List<Car> cars = new ArrayList<>();

    // ======================= ASSOCIAÇÃO: MANY TO ONE ===============================
    @ManyToOne//Muitos employees para uma company (empresa)
    private Company company;

    // ======================= ASSOCIAÇÃO: MANY TO MANY ===============================
    @ManyToMany
    @JoinTable(
            name = "ob_employee_projects", //Nome da nova tabela que irá ter as PK das entidades
            joinColumns = @JoinColumn(name = "employee_id"), //nome da col da entidade que mantem a relação
            inverseJoinColumns = @JoinColumn(name = "project_id") // nome da col da outra entidade da
    )
    private List<Project> projects = new ArrayList<>();

    // Constructors
    public Employee() {}

    public Employee(String firstName, String lastName, String email, Integer age, Double salary,
                    Boolean married, LocalDate birthDate, LocalDateTime registerDate) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.salary = salary;
        this.married = married;
        this.birthDate = birthDate;
        this.registerDate = registerDate;
    }

    public Employee(Long id, String firstName, String lastName, String email, Integer age, Double salary,
                    Boolean married, LocalDate birthDate, LocalDateTime registerDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.salary = salary;
        this.married = married;
        this.birthDate = birthDate;
        this.registerDate = registerDate;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Boolean getMarried() {
        return married;
    }

    public void setMarried(Boolean married) {
        this.married = married;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getNickNames() {
        return nickNames;
    }

    public void setNickNames(List<String> nickNames) {
        this.nickNames = nickNames;
    }

    public List<Integer> getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(List<Integer> creditCards) {
        this.postalCode = creditCards;
    }

    public Set<String> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(Set<String> creditCards) {
        this.creditCards = creditCards;
    }

    public Map<String, String> getPhones() {
        return phones;
    }

    public void setPhones(Map<String, String> phones) {
        this.phones = phones;
    }

    public EmployeeCategory getCategory() {
        return category;
    }

    public void setCategory(EmployeeCategory category) {
        this.category = category;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public LocalDateTime getEditDate() {
        return editDate;
    }

    public void setEditDate(LocalDateTime editDate) {
        this.editDate = editDate;
    }

    @Override
    public String toString() {
        return "Employee {\n" +
                "  id: " + id + ",\n" +
                "  firstName: '" + firstName + "',\n" +
                "  lastName: '" + lastName + "',\n" +
                "  email: '" + email + "',\n" +
                "  age: " + age + ",\n" +
                "  salary: " + salary + ",\n" +
                "  married: " + married + ",\n" +
                "  birthDate: " + birthDate + ",\n" +
                "  registerDate: " + registerDate + "\n" +
                '}';
    }


    // ======================= EVENTS (Livecycle Callback) ==================

    /**
     *  Method é executado antes de ser inserir/criar uma entidade Employee na base de dados.
     *  Portanto é ANTES de ser criar o registro na base de dados. É executado apenas 1 vez, que é
     *  quando se cria o registro.
     *
     */
    @PrePersist
    public void prePersist() {
        System.out.println("prePersist");
        this.setRegisterDate(LocalDateTime.now());
        this.setEditDate(LocalDateTime.now());
    }

    /**
     *  Method é executado antes de ser atualizar uma entidade Employee na base de dados.
     *  Portanto é ANTES de ATUALIZAR o registro na base de dados. E é executado todas as vezes
     *  que ocorre modificações de fato dos dados, pois o @PreUpdate é chamado somente se os dados
     *  forem realmente alterados.
     *
     */
    @PreUpdate
    public void preUpdate(){
        System.out.println("preUpdate");
        this.setEditDate(LocalDateTime.now());
    }

    /**
     * Esse método é executado antes de uma entidade ser removida da base de dados
     */
    @PreRemove
    public void preRemove(){
        System.out.println("preRemove");
    }




}//class
