package br.com.uanderson.springboothibernate.dao;

import br.com.uanderson.springboothibernate.dto.EmployeeDTO;
import br.com.uanderson.springboothibernate.entities.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();
    List<Employee> findAllByJpa();
    List<EmployeeDTO> findAllDTO();
}
