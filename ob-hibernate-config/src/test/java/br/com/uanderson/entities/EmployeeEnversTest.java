package br.com.uanderson.entities;

import br.com.uanderson.dao.CarDAO;
import br.com.uanderson.dao.EmployeeDAO;
import br.com.uanderson.dao.impl.CarDAOImpl;
import br.com.uanderson.dao.impl.EmployeeDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *  REVTYPE:
 *
 *    0 - Criação
 *    1 - Atualização
 *    2 - Deleção
 *
 */
class EmployeeEnversTest {
    EmployeeDAO employeeDAO;
    CarDAO carDAO;

    @BeforeEach
    void setup() {
        employeeDAO = new EmployeeDAOImpl();
        carDAO = new CarDAOImpl();

    }


    @Test
    void createEmployee() {

        Employee employee = new Employee(
                "Employee Audit",
                "Doe",
                "employeeaudit@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                null
        );

        employeeDAO.create(employee);

        employee.setAge(31);
        employee.setSalary(32_000D);

        employeeDAO.update(employee);

        Car car1 = new Car(null, "Ford", 1.2, 2005);
        Car car2 = new Car(null, "Alfa", 2.4, 1990);
        Car car3 = new Car(null, "Toyota", 1.8, 2010);

        carDAO.create(car1);
        carDAO.create(car2);
        carDAO.create(car3);

        employee.getCars().add(car1);
        employee.getCars().add(car2);
        employee.getCars().add(car3);

        employee.setSalary(52_000D);
        employeeDAO.update(employee);

        employee.getCars().clear();

        employee.setSalary(72_000D);
        employeeDAO.update(employee);

//        employeeDAO.deleteById(employee.getId());


    }//method




}//class