package br.com.uanderson.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * User: representa uma entidade usuário com os seguintes atributos id, nome,
 * sobrenome, DNI, se está ativo sim ou não, data de nascimento.
 */
@Entity
@Table(name = "ob_users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String dni; //Documento Nacional de Identidade.

    private Boolean active;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    // Associações
    /*
        Um usuário tem uma informação de faturamento (BillingInfo) e uma informação
        de faturamento só pode pertencer a um mesmo usuário: OneToOne
     */
    @JsonIgnoreProperties("user")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_info_id", unique = true)
    private BillingInfo billingInfo;

    /*
        Um usuário tem muitas tarefas, uma tarefa só pode pertencer a um
        mesmo usuário de cada vez: OneToMany
     */
    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;


    // Constructs
    public User() {
        this.tasks = new ArrayList<>();
    }

    public User(Long id, String firstName, String lastName, String dni, Boolean active, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.active = active;
        this.birthDate = birthDate;
        this.tasks = new ArrayList<>();

    }

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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public BillingInfo getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dni='" + dni + '\'' +
                ", active=" + active +
                ", birthDate=" + birthDate +
                '}';
    }
}//class
/**
 *
 * Explicação do problema de referência cíclica nas relações bidirecionais entre:
 *  [ Task e User ] e  [ BillingInfo e User ]
 *
 * A anotação `@JsonIgnoreProperties("user")` é usada para resolver um problema comum de referência circular entre
 * entidades que possuem relacionamentos bidirecionais, como o caso de `User` e `Task`. Quando você faz uma
 * solicitação ao endpoint `/api/users`, o Jackson (biblioteca usada para serialização/desserialização de
 * JSON no Spring) tenta converter a entidade `User` para JSON, e, ao chegar no relacionamento `tasks`,
 * ele tenta serializar a lista de tarefas associadas. Cada tarefa (`Task`), por sua vez, tem uma referência
 * de volta para o `User`, e isso cria um ciclo infinito de serialização, o que resulta em uma exceção ou uma
 * resposta JSON muito grande e desnecessária.
 *
 * Ao usar `@JsonIgnoreProperties("user")` na lista `tasks`, você está dizendo para o Jackson ignorar a
 * propriedade `user` dentro de cada `Task` ao serializar o `User`. Isso interrompe o ciclo de referência,
 * evitando que a serialização tente incluir o `User` dentro de cada `Task` e, consequentemente, o `Task`
 * dentro de `User` novamente, ad infinitum.
 *
 * Em resumo, essa anotação resolve o problema de referência cíclica, garantindo que quando você buscar um
 * `User` pelo endpoint `/api/users`, as tarefas relacionadas (`tasks`) serão incluídas no JSON de resposta,
 * mas sem incluir de volta o `User` dentro de cada `Task`, evitando ciclos e melhorando a performance
 * e clareza do JSON retornado.
 *
 * ### Explicação simplificada:
 *
 * - **Problema**: O Jackson tenta serializar a relação bidirecional `User` ↔ `Task`,
 *     gerando um ciclo infinito que causa erros.
 *
 * - **Solução**: A anotação `@JsonIgnoreProperties("user")` na propriedade `tasks` diz ao Jackson para
 *      ignorar a referência de volta ao `User` dentro de cada `Task`, evitando o ciclo.
 *
 * - **Resultado**: Quando você faz uma requisição ao `/api/users`, você obtém o `User` com sua lista de
 *      tarefas (`tasks`), mas sem criar um ciclo que incluiria o `User` novamente dentro de cada `Task`.
 */
