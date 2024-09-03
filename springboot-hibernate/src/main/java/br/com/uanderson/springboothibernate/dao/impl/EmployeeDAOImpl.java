package br.com.uanderson.springboothibernate.dao.impl;

import br.com.uanderson.springboothibernate.dao.EmployeeDAO;
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
        return session.createQuery("from Employee", Employee.class).list();
    }

    private List<Employee> findAllByJpa(){
        return entityManager.createQuery("from Employee", Employee.class).getResultList();
    }


}//class
