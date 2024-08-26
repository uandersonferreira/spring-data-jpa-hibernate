package br.com.uanderson.dao;

import br.com.uanderson.dao.impl.EmployeeDAOImpl;
import br.com.uanderson.dto.EmployeeDTO;
import br.com.uanderson.entities.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NativeQueryTest {

    @Test
    void  findByIdNative(){
        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
        EmployeeDTO employeeDTO = employeeDAO.findByIdNative(1L);
        System.out.println(employeeDTO);
    }

    @Test
    void  findAvgByAgeCriteria(){
        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
        Double avg = employeeDAO.findAvgByAgeCriteria();
        System.out.println(avg);
    }

    @Test
    void findEmployeesWithAboveAverageSalary(){
        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
        List<Employee> employees = employeeDAO.findEmployeesWithAboveAverageSalary();
        System.out.println(employees);
    }




}
