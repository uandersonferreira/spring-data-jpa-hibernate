package br.com.uanderson.relationships;

import br.com.uanderson.dao.CompanyDAOImpl;
import br.com.uanderson.dao.EmployeeDAOImpl;
import br.com.uanderson.dao.ProjectDAOImpl;
import br.com.uanderson.entities.Company;
import br.com.uanderson.entities.Employee;
import br.com.uanderson.entities.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ManyToManyTest {
    @Test
    @DisplayName("Teste para provar a associação Many One Many entre Employee e Project")
    void manyToOneTest() {

        Project project1 = new Project(null, "Project 1", LocalDate.now());
        Project project2 = new Project(null, "Project 2", LocalDate.now());
        Project project3 = new Project(null, "Project 3", LocalDate.now());

        Employee employee1 = new Employee(
                "Employee many to many 1",
                "Doe",
                "employeemanytomany1@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now()
        );

        Employee employee2 = new Employee(
                "Employee many to many 2",
                "Doe",
                "employeemanytomanye2@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now()
        );

        employee1.getProjects().add(project1);
        employee1.getProjects().add(project2);

        employee2.getProjects().add(project2);
        employee2.getProjects().add(project3);

        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
        ProjectDAOImpl projectDAO = new ProjectDAOImpl();

        projectDAO.create(project1);
        projectDAO.create(project2);
        projectDAO.create(project3);

        employeeDAO.create(employee1);
        employeeDAO.create(employee2);
    }

}//class
