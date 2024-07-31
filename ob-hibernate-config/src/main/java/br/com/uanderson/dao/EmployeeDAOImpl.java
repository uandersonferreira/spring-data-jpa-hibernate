package br.com.uanderson.dao;

import br.com.uanderson.entities.Employee;
import br.com.uanderson.util.HibernateUtil;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;

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
    public List<Employee> findAllWithCriteria() {
        // Obtém a sessão do Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();

        // 1. Criteria
        CriteriaBuilder builder = session.getCriteriaBuilder();// Obtém o CriteriaBuilder a partir da sessão

        CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);//// Cria uma CriteriaQuery para a entidade Employee

        criteria.select(criteria.from(Employee.class));//select * from ob_employees

        // 2. Query
        List<Employee> employees = session.createQuery(criteria).list();


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
    public Employee findByIdCriteria(Long id) {
        // Obtém a sessão do Hibernate
        Session session = HibernateUtil.getSessionFactory().openSession();

        // 1. Criteria
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();// Obtém o CriteriaBuilder a partir da sessão

        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);//// Cria uma CriteriaQuery para a entidade Employee

        Root<Employee> root = criteriaQuery.from(Employee.class);

        Predicate predicateFilter = criteriaBuilder.equal(root.get("id"), id);

        criteriaQuery.select(root).where(predicateFilter);//select * from ob_employees

        // 2. Query
        Employee employee = session.createQuery(criteriaQuery).getSingleResult();//Para 1 registro

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
    public List<Employee> findByLastNameLikeCriteria(String lastName) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // 1. Criteria
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> root = criteriaQuery.from(Employee.class);

        //Equivalente ao contains do Java
        //select * from ob_employees where last_name LIKE '%:lastName%'
        Predicate predicateFilter = criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");

        criteriaQuery.select(root).where(predicateFilter);

        // 2. Query
        List<Employee> employees = session.createQuery(criteriaQuery).list();

        session.close();

        return employees;
    }

    @Override
    public List<Employee> findByAgeGreaterCriteria(Integer age) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // 1. Criteria
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> root = criteriaQuery.from(Employee.class);

        Predicate predicateFilter = criteriaBuilder.greaterThan(root.get("age"), age);

        criteriaQuery.select(root).where(predicateFilter);

        // 2. Query
        List<Employee> employees = session.createQuery(criteriaQuery).list();

        session.close();

        return employees;
        /*
        .gt() é a abreviação de greaterThan():
           - Create a predicate for testing whether the first argument is greater than the second.
         */
    }

    @Override
    public List<Employee> findByAgeBetweenCriteria(Integer min, Integer max) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // 1. Criteria
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> root = criteriaQuery.from(Employee.class);

        Predicate predicateFilter = criteriaBuilder.between(root.get("age"), min, max);

        criteriaQuery.select(root).where(predicateFilter);

        // 2. Query
        List<Employee> employees = session.createQuery(criteriaQuery).list();

        session.close();

        return employees;
        /*
        .gt():
           - Create a predicate for testing whether the first argument is greater than the second.
         */
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
