package br.com.uanderson.dao.impl;

import br.com.uanderson.dao.EmployeeDAO;
import br.com.uanderson.dto.EmployeeDTO;
import br.com.uanderson.dto.EmployeeProjectionDTO;
import br.com.uanderson.entities.Employee;
import br.com.uanderson.entities.EmployeeCategory;
import br.com.uanderson.util.HibernateUtil;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.ArrayList;
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
    public List<Employee> findAllNative() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        NativeQuery<Employee> nativeQuery = session.createNativeQuery("SELECT * FROM ob_employees", Employee.class);

        List<Employee> employees = nativeQuery.list();

        session.close();

        return employees;
    }

    @Override
    public List<Employee> findMostPaid() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Query<Employee> namedQuery = session.createNamedQuery("Employee.mostPaid", Employee.class);

        List<Employee> employees = namedQuery.getResultList();

        session.close();

        return employees;
        /*

         */
    }

    @Override
    public List<EmployeeProjectionDTO> findAllProjectionNative() { //Ex. com um objeto DTO personalizado com as projections
        Session session = HibernateUtil.getSessionFactory().openSession();

        NativeQuery nativeQuery = session.createNativeQuery("SELECT id, email, age from ob_employees", EmployeeProjectionDTO.class);

        List<EmployeeProjectionDTO> employees = nativeQuery.list();

        session.close();

        return employees;
    }

    @Override
    public List<EmployeeProjectionDTO> findAllProjectionEmployeeNative() { //Ex. sem a criação de um DTO personalizado
        Session session = HibernateUtil.getSessionFactory().openSession();

        NativeQuery nativeQuery = session.createNativeQuery("SELECT id, email, age from ob_employees");

        List<Object[]> employees = nativeQuery.list();
        List<EmployeeProjectionDTO> employeeDtos = new ArrayList<>();

        for (Object[] employee : employees) {
            Long id = (Long) employee[0];
            String email = (String) employee[1];
            Integer age = (Integer) employee[2];

            employeeDtos.add(new EmployeeProjectionDTO(id, email, age));
        }

        session.close();

        return employeeDtos;
    }

    @Override
    public Long count() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery("select count(e) from Employee e");

        Long count = (Long) query.getSingleResult();

        session.close();

        return count;
        /*
            Criar um method count, que retorne a quantidade de employees no banco de dados é
            útil para substituir a operação de findAll que fariamos, para recuperar todos os registros
            e depois retornar o tamanho da lista.
            - Outro caso de uso, contabilizar employees ativos ou inativos, de lincença ...
         */
    }


    @Override
    public Employee findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Employee employee = session.find(Employee.class, id);

        session.close();

        return employee;
    }

    @Override
    public Employee findByIdEager(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //Consulta HQL (usa o código java na consulta)
        Query<Employee> query = session.createQuery(
                "select distinct e from Employee e join fetch e.cars where e.id = :pk",
                Employee.class
        );

        query.setParameter("pk", id);//nome não importa, desde que seja o mesmo. mas boas pratica de nome deve ser usadas

        Employee employee = query.getSingleResult();

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
    public EmployeeDTO findByIdNative(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Não pode ter espaço entre ":" e "id"
        NativeQuery<EmployeeDTO> query = session.createNativeQuery(
                "SELECT id, email FROM ob_employees WHERE id = :id",
                EmployeeDTO.class
        );

        // Define o parâmetro 'id'
        query.setParameter("id", id);

        // Obtem o resultado único
        EmployeeDTO employee = query.getSingleResult();

        session.close();

        return employee;
    }

    @Override
    public List<Employee> findByAge(Integer age) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //Consulta HQL, com Named Parameter
        Query<Employee> query = session.createQuery("from Employee  where age =: age", Employee.class);// =: age (também pode ser escrito como param1)
        query.setParameter("age", age);

        //Consulta HQL, com Position Parameter
        Query<Employee> query2 = session.createQuery("from Employee  where age = ?1", Employee.class);
        //Query<Employee> query2 = session.createQuery("from Employee  where age = ?1 and salary <= ?2", Employee.class);
        query2.setParameter(1, age);
        //query2.setParameter(2, 60000); //caso tenha mais de um filtro na consulta

        List<Employee> employees = query2.list();

        session.close();

        return employees;
                /*
                    Resumo: podemos criar consultas sql usando Named Parameter (usando o nome dos atributos da class/tabela)
                    ou Position Parameter (informando com um número a posição de cada parametro na consulta).

                    - Particulamente, pelo o nome é mais fácil de identificar :)
                    ex: ... variavel_da_tabela =: nome_do_parametro and ...
                 */
    }

    /**
     * Operação de agregação - Média (AVG)
     *
     * @return
     */
    @Override
    public Double findAvgByAgeCriteria() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Double> criteriaQuery = builder.createQuery(Double.class);//nossa Consulta SQL irá retornar um valor e não o objeto Employee (RESULTADO)
        Root<Employee> root = criteriaQuery.from(Employee.class);//Mas aqui é onde iremos travbalhar, fazer as consultas no caso e na tavela Employee mesmo

        Expression<Double> avg = builder.avg(root.get("age"));

        criteriaQuery.select(avg);

        Double averageAge = session.createQuery(criteriaQuery).getSingleResult();

        return averageAge;
    }

    @Override
    public List<Employee> findEmployeesWithAboveAverageSalary() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Builder para criar consultas
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Consulta principal que retorna a lista de empregados
        CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);

        // Definindo a tabela (Root) da consulta principal
        Root<Employee> root = criteriaQuery.from(Employee.class);

        // criteriaQuery.subquery é usado para criar Subconsulta, que permita calcular a média dos salários
        Subquery<Double> subquery = criteriaQuery.subquery(Double.class);
        Root<Employee> subRoot = subquery.from(Employee.class);
        subquery.select(builder.avg(subRoot.get("salary")));

        // Condição: Seleciona empregados cujo salário seja maior do que a média
        criteriaQuery.select(root).where(builder.greaterThan(root.get("salary"), subquery));

        // Executa a consulta
        List<Employee> employees = session.createQuery(criteriaQuery).getResultList();

        session.close();

        return employees;
        /*
        O método criteriaQuery.subquery() em Criteria API permite criar uma subconsulta dentro de
        uma consulta principal. Isso é útil quando você precisa executar uma consulta aninhada,
        como quando uma condição na consulta principal depende dos resultados de outra consulta.

        SELECT *
        FROM ob_employees e
        WHERE e.salary > (SELECT AVG(salary) FROM ob_employees);


         */
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
    public List<Employee> findByAgeBetweenAndCategoryCriteria(Integer ageMin, Integer ageMax,
                                                              EmployeeCategory category) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // 1. CriteriaBuilder
        //HibernateCriteriaBuilder builder = session.getCriteriaBuilder(); embora retorne um 'HibernateCriteriaBuilder' estamos usando a interface CriteriaBuilder que o mesmo implementa
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);

        Predicate ageFilter = builder.between(root.get("age"), ageMin, ageMax);
        Predicate categoryFilter = builder.equal(root.get("category"), category);
        Predicate filterFinal = builder.and(ageFilter, categoryFilter); // (AND) Estamos juntando os filtros em um só para poder passar para o 'where' do sql
        //Predicate filterFinal = builder.or(ageFilter, categoryFilter); // (OR)

        //criteriaQuery.select(root).where(builder.and(ageFilter, categoryFilter));
        criteriaQuery.select(root).where(filterFinal);

        // 2. Query
        List<Employee> employees = session.createQuery(criteriaQuery).list();

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
