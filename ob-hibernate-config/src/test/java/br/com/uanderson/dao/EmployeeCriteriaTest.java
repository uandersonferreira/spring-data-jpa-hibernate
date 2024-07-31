package br.com.uanderson.dao;

import br.com.uanderson.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes para as operações Criteria
 */
class EmployeeCriteriaTest {
    EmployeeDAO dao;

    @BeforeEach
    void setUp() {
        dao = new EmployeeDAOImpl();
    }

    @Test
    void findAllCriteria() {
        List<Employee> employees = dao.findAllWithCriteria();
        System.out.println(employees);
    }

    @Test
    void findByIdCriteria() {
        Employee employee = dao.findByIdCriteria(1L);
        System.out.println(employee);
    }

    @Test
    void findByLastNameLikeCriteria() {
        List<Employee> employees = dao.findByLastNameLikeCriteria("smith");
        System.out.println(employees);
    }

    @Test
    void findByAgeGreaterCriteria() {
        List<Employee> employees = dao.findByAgeGreaterCriteria(30);
        System.out.println(employees);
    }

    @Test
    void findByAgeBetweenCriteria() {
        List<Employee> employees = dao.findByAgeBetweenCriteria(19, 30);
        System.out.println(employees);
    }
}//class