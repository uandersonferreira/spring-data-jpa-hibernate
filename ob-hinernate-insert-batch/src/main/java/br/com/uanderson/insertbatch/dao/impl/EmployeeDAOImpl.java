package br.com.uanderson.insertbatch.dao.impl;

import br.com.uanderson.insertbatch.dao.EmployeeDAO;
import br.com.uanderson.insertbatch.entities.Employee;
import br.com.uanderson.insertbatch.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {


    @Override
    public void saveEmployees(List<Employee> employees) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            //guardar os employees
            session.beginTransaction();

            for (Employee employee : employees) {
                session.persist(employee);
            }

            session.getTransaction().commit();

        }catch (RuntimeException e){
            if (session.getTransaction() != null && session.getTransaction().isActive()){
                session.getTransaction().rollback();
            }

            throw e;

        }finally {
            if (session != null){
                session.close();
            }
        }


    }
}//class
