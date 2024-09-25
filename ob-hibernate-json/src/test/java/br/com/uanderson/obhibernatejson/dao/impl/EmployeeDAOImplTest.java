package br.com.uanderson.obhibernatejson.dao.impl;

import br.com.uanderson.obhibernatejson.dao.EmployeeDAO;
import br.com.uanderson.obhibernatejson.entities.Employee;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOImplTest {

    @Test
    void create(){
        EmployeeDAO employeeDAO = new EmployeeDAOImpl();

        Map<String, Object> json = new HashMap<>();
        json.put("color", "azul");
        json.put("number", 40);

        Employee employee = new Employee(null, "John", "Doe", "johndoe@example.com", json);

        employeeDAO.create(employee);

        System.out.println(employeeDAO.findAll());


    }//method

}//class