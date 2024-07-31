package br.com.uanderson.dao;

import br.com.uanderson.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
/**
 * Testes para as operações CRUD
 */
class EmployeeCRUDTest {
    EmployeeDAO dao;

    @BeforeEach
    void setUp() {
        dao = new EmployeeDAOImpl();
    }

    @Test
    void findAll() {
        List<Employee> employees = dao.findAll();
        System.out.println(employees);
    }

    @Test
    void findById() {
        Employee employee1 = dao.findById(1L);
        Employee employee2 = dao.findById(15L);//retorna null se não encontrar ninguém
        Employee employee3 = dao.findById(3L);

    }

    @Test
    void findByAge() {
        List<Employee> employees35 = dao.findByAge(35);
        List<Employee> employees45 = dao.findByAge(45);
        List<Employee> employees50 = dao.findByAge(50);
    }

    @Test
    void create() {
        Employee employee = new Employee("Uanderson", "Oliveira", "uanderson@example.com",
                21, 5000.0, false, LocalDate.of(2002, 8, 13),
                LocalDateTime.now());

        Employee createdEmployee = dao.create(employee);

        System.out.println(createdEmployee);
    }

    @Test
    void update() {
        Employee employeeToUpdate = dao.findById(1L);
        System.out.println("employeeToUpdate: " + employeeToUpdate);
        employeeToUpdate.setFirstName("Uanderson Updated");

        Employee employeeAtualizado = dao.update(employeeToUpdate);
        System.out.println("employeeAtualizado: " + employeeAtualizado);
    }

    @Test
    void deleteById() {
        boolean isDelete = dao.deleteById(1L);
        System.out.println("isDelete: " + isDelete);
    }
}