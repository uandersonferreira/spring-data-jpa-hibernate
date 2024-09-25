package br.com.uanderson.obhibernatejson.entities;

import br.com.uanderson.obhibernatejson.converts.JsonConvert;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Map;


@Entity
@Table(name = "ob_employees") //Optional
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

    @Convert(converter = JsonConvert.class)
    private Map<String, Object> json;


    // Constructors
    public Employee() {}

    public Employee(Long id, String firstName, String lastName, String email, Map<String, Object> json) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.json = json;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<String, Object> getJson() {
        return json;
    }

    public void setJson(Map<String, Object> json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "Employee {\n" +
                "  id: " + id + ",\n" +
                "  firstName: '" + firstName + "',\n" +
                "  lastName: '" + lastName + "',\n" +
                "  email: '" + email + "',\n" +
                "  json: '" + json + "',\n" +
                '}';
    }


}//class
