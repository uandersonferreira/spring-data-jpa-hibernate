package br.com.uanderson.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "app_seq_id")
    private Long id;


}
