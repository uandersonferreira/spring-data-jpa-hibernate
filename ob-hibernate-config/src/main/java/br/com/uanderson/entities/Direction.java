package br.com.uanderson.entities;


import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ob_directions")
@Audited
public class Direction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String country;
    @Column(name = "create_on")
    @CreationTimestamp
    private LocalDateTime createOn;

    public Direction() {
    }

    public Direction(Long id, String street, String city, String country) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreateOn() {
        return createOn;
    }

    public void setCreateOn(LocalDateTime createOn) {
        this.createOn = createOn;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}//class
