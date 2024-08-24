package br.com.uanderson.relationships;

import br.com.uanderson.dao.CompanyDAOImpl;
import br.com.uanderson.dao.EmployeeDAOImpl;
import br.com.uanderson.entities.Company;
import br.com.uanderson.entities.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ManyToOneTest {
    @Test
    @DisplayName("Teste para provar a associação Many One to entre Employee e Company")
    void manyToOneTest() {

        Company company = new Company(null, "ABC Company", "ABC Inc.", 100_000_000D,2000);

        Employee employee1 = new Employee(
                "Employee many to one 1",
                "Doe",
                "employeemanytoone1@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now()
        );

        Employee employee2 = new Employee(
                "Employee many to one 2",
                "Doe",
                "employeemanytoone2@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now()
        );

        employee1.setCompany(company);
        employee2.setCompany(company);

        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
        CompanyDAOImpl companyDAO = new CompanyDAOImpl();

        companyDAO.create(company);

        employeeDAO.create(employee1);
        employeeDAO.create(employee2);

        Employee employeeDB = employeeDAO.findById(6L);
        System.out.println(employeeDB.getCompany());




    }

}//class
