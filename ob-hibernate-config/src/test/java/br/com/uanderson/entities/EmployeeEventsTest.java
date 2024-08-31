package br.com.uanderson.entities;

import br.com.uanderson.dao.CarDAO;
import br.com.uanderson.dao.EmployeeDAO;
import br.com.uanderson.dao.impl.CarDAOImpl;
import br.com.uanderson.dao.impl.EmployeeDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

class EmployeeEventsTest {
    EmployeeDAO employeeDAO;
    CarDAO carDAO;

    @BeforeEach
    void setup() {
        employeeDAO = new EmployeeDAOImpl();
        carDAO = new CarDAOImpl();

    }


    @Test
    void prePersist() {

        Employee employee = new Employee(
                null,
                "Doe",
                "employee4@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                null
        );

        employeeDAO.create(employee);

        /*
          Verifique nos logs se o método prePersist é invocado e um
           data para o campo RegisterDate.
         */

    }//method

    @Test
    void preUpdate() {
        Employee employee = employeeDAO.findById(1L);

        employee.setAge(45);

        employeeDAO.update(employee);

        /*
            Verificar nos logs que será executado o method preUpdate(), antes de ser atualizar
            o registro na base de dados. @PreUpdate é chamado somente se os dados forem realmente alterados, ou seja,
            mesmo que seja uma operação de update se os dados permanecem os mesmo, não é chamado.
         */

    }//method

    @Test
    void preRemove() {

        Employee employee = new Employee(
                "Employee one to Many",
                "Doe",
                "employeeonetomany@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now()
        );

        Car car1 = new Car(null, "Ford", 1.2, 2005);
        Car car2 = new Car(null, "Alfa", 2.4, 1990);
        Car car3 = new Car(null, "Toyota", 1.8, 2010);

        carDAO.create(car1);
        carDAO.create(car2);
        carDAO.create(car3);

        employee.getCars().add(car1);
        employee.getCars().add(car2);
        employee.getCars().add(car3);

        employeeDAO.create(employee);

        employeeDAO.deleteById(employee.getId());
    }


}//class