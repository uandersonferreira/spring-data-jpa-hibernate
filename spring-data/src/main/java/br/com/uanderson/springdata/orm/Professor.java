package br.com.uanderson.springdata.orm;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "professores")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String prontuario;
    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)//ON DELETE SET NULL
    private List<Disciplina> disciplinas;

    public Professor(Long id, String name, String prontuario) {
        this.id = id;
        this.name = name;
        this.prontuario = prontuario;
    }

    public Professor( String name, String prontuario) {
        this.name = name;
        this.prontuario = prontuario;
    }
    @Deprecated//para partes do código obsolentas, para indicar que não usaremos muito, ou que será usado por outra biblioteca externa.
    public Professor() {}//O hibernate exiger um construtor vazio

    @PreRemove // Indica para o spring que Antes que uma entidade seja removida deve-ser setar 'null' para determinado campo.
    public void atualizazDisciplinasOuRemove(){
        //ON DELETE SET NULL
        System.out.println("******  atualizazDisciplinasOuRemove ******");
        for (Disciplina disciplina : getDisciplinas()) {
            disciplina.setProfessor(null);
        }
    }//method
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prontuario='" + prontuario + '\'' +
                '}';
    }

}//class
