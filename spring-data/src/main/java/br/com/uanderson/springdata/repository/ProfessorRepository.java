package br.com.uanderson.springdata.repository;

import br.com.uanderson.springdata.orm.Professor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, Long> {
}
//<Classe que estamos criando as operações de crud e o tipo da chave primária>
