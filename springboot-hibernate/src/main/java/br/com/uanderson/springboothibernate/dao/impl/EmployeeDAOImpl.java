package br.com.uanderson.springboothibernate.dao.impl;

import br.com.uanderson.springboothibernate.dao.EmployeeDAO;
import br.com.uanderson.springboothibernate.dto.EmployeeDTO;
import br.com.uanderson.springboothibernate.entities.Employee;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA
 * HIBERNATE
 */
@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
    private final Session session; // Hibernate (por ser gerenciada pelo spring não precisamos abrir/fechar)
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

    @Override
    public List<Employee> findByName(String name) {
        // Para demonstrar o uso dos indices.
        long start = System.currentTimeMillis();

        Query<Employee> query = session.createQuery("from Employee where name = :name", Employee.class);
        query.setParameter("name", name);
        List<Employee> employees = query.getResultList();
        long end = System.currentTimeMillis();
        System.out.println("Time total findAllDTO(): " + (end - start) + " ms");

        return employees;
    }

    @Override
    public List<Employee> findAllLastPage() {
        // Define o tamanho da página, ou seja, o número de registros por página
        int size = 20;

        // Consulta para contar o número total de registros na tabela Employee
        String countHQL = "SELECT count(e.id) FROM Employee e";

        // Executa a consulta para obter a contagem total de registros
        Long countResult = (Long) session.createQuery(countHQL).uniqueResult(); // Ex: 200_000 registros

        // Calcula o número da última página.
        // O método Math.ceil() garante que o resultado seja arredondado para cima, caso haja registros "extras"
        // (se countResult não for um múltiplo exato de size)
        int lastPageNum = (int) Math.ceil(countResult / (double) size);

        // Cria a consulta para recuperar os empregados da tabela Employee
        Query query = session.createQuery("from Employee");

        // Define o primeiro registro que será retornado. Como estamos buscando a última página,
        // multiplicamos o número da última página - 1 (porque a contagem começa em 0) pelo tamanho da página
        query.setFirstResult((lastPageNum - 1) * size);

        // Define o número máximo de resultados que queremos obter, que é o tamanho da página
        query.setMaxResults(size);

        // Executa a consulta e recupera a lista de empregados da última página
        List<Employee> lastPageEmployees = query.list();

        // Retorna a lista de empregados da última página
        return lastPageEmployees;
    }

}//class
