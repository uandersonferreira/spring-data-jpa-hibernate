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
     * Utiliza HQL
     * @return lista de Employee
     */
    List<Employee> findAll();

    /**
     * Recuperar todos os empregados da base de dados da tabela employees
     * Utiliza Criteria
     * @return lista de Employee
     */
    List<Employee> findAllWithCriteria();


    /**
     * Busca um empregado pelo seu Id
     * Utiliza métodos da implementação Hibernate
     * @return um Employee se exits or null se não encontrar ninguém
     */
    Employee findById(Long id);

    /**
     * Busca um empregado pelo seu Id é seus objetos de suas relações many
     * Utiliza métodos da implementação Hibernate
     * @return um Employee se exits or null se não encontrar ninguém
     */
    Employee findByIdEager(Long id);

    /**
     * Busca um empregado pelo seu Id
     * Utiliza JPA Criteria API
     * @return um Employee se exits or null se não encontrar ninguém
     */
    Employee findByIdCriteria(Long id);

    /**
     * Busca todos os empregados pela sua idade
     *
     * @param age
     * @return uma lista de Employee
     */
    List<Employee> findByAge(Integer age);

    /**
     * Filtra pelo sobrenome
     * @param lastName
     * @return lista de empregados
     */
    List<Employee> findByLastNameLikeCriteria(String lastName);

    /**
     * Filtra pela idade. Maior que determinado número
     * @param age
     * @return lista de empregados
     */
    List<Employee> findByAgeGreaterCriteria(Integer age);

    /**
     * Filtra por um intervalo de idades entre dois números
     * @param min
     * @param max
     * @return lista de empregados
     */
    List<Employee> findByAgeBetweenCriteria(Integer min, Integer max);


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
