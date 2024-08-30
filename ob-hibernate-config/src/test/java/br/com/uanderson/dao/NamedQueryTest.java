package br.com.uanderson.dao;

import br.com.uanderson.dao.impl.EmployeeDAOImpl;
import br.com.uanderson.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NamedQueryTest {
    EmployeeDAOImpl dao;

    @BeforeEach
        //executa antes de cada test
    void setUp() {
        dao = new EmployeeDAOImpl();
    }

    @Test
    void findMostPaid() {
        List<Employee> employees = dao.findMostPaid();
        System.out.println(employees);
    }


}
