package br.com.uanderson.springboothibernate.dao;

import br.com.uanderson.springboothibernate.dto.EmployeeDTO;
import br.com.uanderson.springboothibernate.entities.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();
    List<Employee> findAllByJpa();
    List<EmployeeDTO> findAllDTO();

    // Para demonstrar o uso dos indices.
    List<Employee> findByName(String name);

    //Para demonstrar o uso de paginação
    List<Employee> findAllLastPage();

    //Para demonstrar o uso de transaction
    Employee save(Employee employee);


}
