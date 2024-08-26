package br.com.uanderson.relationships;

import br.com.uanderson.dao.impl.DirectionDAOImpl;
import br.com.uanderson.dao.impl.EmployeeDAOImpl;
import br.com.uanderson.entities.Direction;
import br.com.uanderson.entities.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OneToOneTest {

    @Test
    @DisplayName("Teste para provar a associação One to One entre Employee e Direction")
    void employeeDirectionTest(){
        Direction direction = new Direction(null, "ELM street", "cansas", "Croacia");

        Employee employee = new Employee(
                "Employee one to one",
                "Doe",
                "employeeonetoone@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now()
        );

        employee.setDirection(direction);

        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
        DirectionDAOImpl directionDAO = new DirectionDAOImpl();

        directionDAO.create(direction);
        employeeDAO.create(employee);

    }

}//class
