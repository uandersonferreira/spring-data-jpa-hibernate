package br.com.uanderson.insertbatch.dao;


import br.com.uanderson.insertbatch.entities.Employee;

import java.util.List;


public interface EmployeeDAO {

    void saveEmployees(List<Employee> employees);

}
