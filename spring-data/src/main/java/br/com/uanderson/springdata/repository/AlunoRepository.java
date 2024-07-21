package br.com.uanderson.springdata.repository;

import br.com.uanderson.springdata.orm.Aluno;
import br.com.uanderson.springdata.orm.Disciplina;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends CrudRepository<Aluno, Long> {

    List<Aluno> findByNomeStartingWith(String nome);
    List<Aluno> findByNomeStartingWithAndIdadeLessThanEqual(String nome, Integer idade);
}
//<Classe que estamos criando as operações de crud e o tipo da chave primária>
