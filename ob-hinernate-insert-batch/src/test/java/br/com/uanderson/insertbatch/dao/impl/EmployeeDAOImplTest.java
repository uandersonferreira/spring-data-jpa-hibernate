package br.com.uanderson.insertbatch.dao.impl;


import br.com.uanderson.insertbatch.dao.EmployeeDAO;
import br.com.uanderson.insertbatch.entities.Employee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class EmployeeDAOImplTest {

    //100_000 - 40s
    @Test
    void saveEmployees(){
        EmployeeDAO dao =  new EmployeeDAOImpl();

        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < 100_000; i++) {
            employees.add(new Employee(null, "emp" + i , "lastname", "email"+i));
        }

        dao.saveEmployees(employees);

    }

}//class
