package br.com.uanderson.dao;

import br.com.uanderson.entities.Employee;

import java.util.List;

/**
 * DAO - Data Access Object
 * <p>
 * Métodos CRUD - Create, Retrieve, Update, Delete
 */
public interface EmployeeDAO {
    /**
     * Recuperar todos os empregados da base de dados da tabela employees
     *
     * @return lista de Employee
     */
    List<Employee> findAll();

    /**
     * Busca um empregado pelo seu Id
     *
     * @return um Employee se exits or null se não encontrar ninguém
     */
    Employee findById(Long id);

    /**
     * Busca todos os empregados pela sua idade
     *
     * @param age
     * @return uma lista de Employee
     */
    List<Employee> findByAge(Integer age);

    /**
     * Inseri um novo registro na tabela employees
     *
     * @param employee
     * @return um Employee
     */
    Employee create(Employee employee);

    /**
     * Atualiza um registro existente na tabela employees
     *
     * @param employee
     * @return um Employee
     */
    Employee update(Employee employee);

    /**
     * Deleta um registro na base de dados da tabela employees
     *
     * @param id
     * @return true se deletado com sucesso
     */
    boolean deleteById(Long id);

}