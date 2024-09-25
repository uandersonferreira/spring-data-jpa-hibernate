package br.com.uanderson.obhibernatejson.dao.impl;


import br.com.uanderson.obhibernatejson.dao.EmployeeDAO;
import br.com.uanderson.obhibernatejson.entities.Employee;
import br.com.uanderson.obhibernatejson.util.HibernateUtil;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public List<Employee> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //Consulta HQL
        Query<Employee> query = session.createQuery("from Employee ", Employee.class);
        List<Employee> employees = query.list();

        //Outra opção Equivalente:
        //List<Employee> employee = session.createQuery("from Employee", Employee.class).list();

        session.close();
        return employees;
    }

    @Override
    public Employee create(Employee employee) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.persist(employee);

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro
            e.printStackTrace();
        } finally {
            session.close();
        }

        return employee;
    }
}//class
