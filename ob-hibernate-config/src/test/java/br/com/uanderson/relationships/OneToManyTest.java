package br.com.uanderson.relationships;

import br.com.uanderson.dao.EmployeeDAOImpl;
import br.com.uanderson.entities.Car;
import br.com.uanderson.entities.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OneToManyTest {

    @Test
    @DisplayName("Teste para provar a associação One to Many entre Employee e Car")
    void oneToManyTest(){

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

        employee.getCars().add(car1);
        employee.getCars().add(car2);
        employee.getCars().add(car3);

        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();

        employeeDAO.create(employee);

        //org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: br.com.uanderson.entities.Employee.cars: could not initialize proxy - no Session
        //Por default é LAZY, poderiamos usar o EAGER para acessar a lista, mas não é recomendavel devido o desempenho, pois irá carregar tudo, mesmo se eu não precisar.
        //Solução: Criar um query especifica que atenda a nossa necessidade
        //Employee employeeDB = employeeDAO.findById(1L);
        //System.out.println("Employee DB: "+employeeDB);
        //List<Car> cars = employeeDB.getCars();
        //System.out.println(cars);

        //Solução para evitar LazyInitializationException creamos uma query que carrega os dados que queremos
        //Inner join por debaixo dos panos
        Employee employeeDB = employeeDAO.findByIdEager(6L);
        System.out.println("Employee DB: "+employeeDB);
        List<Car> cars = employeeDB.getCars();
        System.out.println(cars);




    }
}
