package br.com.uanderson.obhibernatejson.dao;


import br.com.uanderson.obhibernatejson.entities.Employee;

import java.util.List;


public interface EmployeeDAO {

    List<Employee> findAll();
    Employee create(Employee employee);

}
