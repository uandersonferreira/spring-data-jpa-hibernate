package br.com.uanderson.entities;

import br.com.uanderson.generators.CustomGeneratorPk;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;

@Entity
@Table(name = "student")//opcional
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Basic//default
    @Column(name = "full_name", nullable = false)
    private String name;
    @NaturalId(mutable = true)//por default é false
    private String email;

    @Transient//Specifies that the property or field is not persistent.
    private String password;
    private LocalDate birthDate;

    @Formula(value = "extract(year from age(birthDate))")
    private int age;


    public Student() {
        //Por default Class 'Student' should have [public, protected] no-arg constructor
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}//class

/*
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq_id")
    @SequenceGenerator(name = "student_seq_id", sequenceName = "name_in_db", initialValue = 20, allocationSize = 1)
    private Long id;
 */