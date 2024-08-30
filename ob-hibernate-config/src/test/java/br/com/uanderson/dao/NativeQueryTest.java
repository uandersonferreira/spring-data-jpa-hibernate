package br.com.uanderson.dao;

import br.com.uanderson.dao.impl.EmployeeDAOImpl;
import br.com.uanderson.dto.EmployeeDTO;
import br.com.uanderson.dto.EmployeeProjectionDTO;
import br.com.uanderson.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NativeQueryTest {
    EmployeeDAOImpl dao;

    @BeforeEach
        //executa antes de cada test
    void setUp() {
        dao = new EmployeeDAOImpl();
    }


    @Test
    void findByIdNative() {
        EmployeeDTO employeeDTO = dao.findByIdNative(1L);
        System.out.println(employeeDTO);
    }

    @Test
    void findAvgByAgeCriteria() {
        Double avg = dao.findAvgByAgeCriteria();
        System.out.println(avg);
    }

    @Test
    void findEmployeesWithAboveAverageSalary() {
        List<Employee> employees = dao.findEmployeesWithAboveAverageSalary();
        System.out.println(employees);
    }

    @Test
    void findAllNative() {
        List<Employee> employees = dao.findAllNative();
        System.out.println(employees);
    }

    @Test
    void findAllProjectionNative() {
        List<EmployeeProjectionDTO> employees = dao.findAllProjectionNative();
        System.out.println(employees);
    }

    @Test
    void findAllProjectionEmployeeNative() {
        List<EmployeeProjectionDTO> employees = dao.findAllProjectionEmployeeNative();
        System.out.println(employees);
    }





}//class
