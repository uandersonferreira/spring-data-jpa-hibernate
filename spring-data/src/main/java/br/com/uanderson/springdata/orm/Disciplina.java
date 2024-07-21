package br.com.uanderson.springdata.orm;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "disciplinas")
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    private Integer semestre;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id", nullable = true)
    private Professor professor;
    @ManyToMany
            @JoinTable(//Define uma nova tabela de Junção
                    name = "disciplinas_alunos", // Nome da tabela de Junçõa
                    joinColumns = @JoinColumn(name = "disciplina_fk"),//Nome da coluna que ficará a primary Key da tabela atual
                    inverseJoinColumns = @JoinColumn(name = "aluno_fk")//Nome da coluna que ficará a primary Key da outra tabela de junção
            )
    Set<Aluno> alunos;

    public Disciplina(String nome, Integer semestre, Professor professor, Set<Aluno> alunos) {
        this.nome = nome;
        this.semestre = semestre;
        this.professor = professor;
        this.alunos = alunos;
    }

    public Disciplina(String nome, Integer semestre, Professor professor) {
        this.nome = nome;
        this.semestre = semestre;
        this.professor = professor;
    }

    @Deprecated
    public Disciplina() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Set<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(Set<Aluno> alunos) {
        this.alunos = alunos;
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", semestre=" + semestre +
                ", professor=" + professor +
                ", alunos=" + alunos +
                '}';
    }
}//class
