package br.com.uanderson.springboothibernate.dao.impl;

import br.com.uanderson.springboothibernate.dao.EmployeeDAO;
import br.com.uanderson.springboothibernate.dto.EmployeeDTO;
import br.com.uanderson.springboothibernate.entities.Employee;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA
 * HIBERNATE
 */
@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
    private final Session session; // Hibernate
    private final EntityManager entityManager; // JPA

    public EmployeeDAOImpl(Session session, EntityManager entityManager) {
        this.session = session;
        this.entityManager = entityManager;
    }

    @Override
    public List<Employee> findAll() {
        long start = System.currentTimeMillis();
        List<Employee> employees = session.createQuery("from Employee", Employee.class).list();
        long end = System.currentTimeMillis();
        System.out.println("Time total findAll(): " + (end - start) + " ms");
        return employees;
    }

    @Override
    public List<Employee> findAllByJpa() {
        long start = System.currentTimeMillis();
        List<Employee> employees = entityManager.createQuery("from Employee", Employee.class).getResultList();
        long end = System.currentTimeMillis();
        System.out.println("Time total findAllByJpa(): " + (end - start) + " ms");
        return employees;
    }

    @Override
    public List<EmployeeDTO> findAllDTO() {
        long start = System.currentTimeMillis();
        List<EmployeeDTO> employeeDTOS = session.createQuery("SELECT new br.com.uanderson.springboothibernate.dto.EmployeeDTO(e.id, e.email) FROM Employee e").list();
        long end = System.currentTimeMillis();
        System.out.println("Time total findAllDTO(): " + (end - start) + " ms");
        return employeeDTOS;
    }

}//class
