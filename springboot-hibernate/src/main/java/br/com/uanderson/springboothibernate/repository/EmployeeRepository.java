package br.com.uanderson.springboothibernate.repository;

import br.com.uanderson.springboothibernate.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Jpa Repository
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Implementa os métodos de busca e CRUD

}
