package br.com.uanderson.dao;

import br.com.uanderson.entities.Employee;
import br.com.uanderson.util.HibernateUtil;
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
    public Employee findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Employee employee = session.find(Employee.class, id);

        session.close();

        return employee;
    }

    @Override
    public List<Employee> findByAge(Integer age) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //Consulta HQL
        Query<Employee> query = session.createQuery("from Employee  where age =: age", Employee.class);// =: age (também pode ser escrito como param1)
        //Query<Employee> query = session.createQuery("from Employee  where age = :param1", Employee.class);//podemos fazer mais filtros caso queiramos 'and ...'

        query.setParameter("age", age);

        List<Employee> employees = query.list();

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
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro
        } finally {
            session.close();
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            //session.update(employee) - @Deprecated(since = "6.0")
            session.merge(employee);//Usar merge() no lugar de update(). (ATUALIZA E INSERE REGISTROS)

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro
        } finally {
            session.close();
        }

        return employee;
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Employee employee = findById(id);

            session.remove(employee);

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro
            return false;
        } finally {
            session.close();
        }

        return true;
    }
}
