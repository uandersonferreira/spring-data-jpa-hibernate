package br.com.uanderson.springboothibernate;

import br.com.uanderson.springboothibernate.dao.EmployeeDAO;
import br.com.uanderson.springboothibernate.dto.EmployeeDTO;
import br.com.uanderson.springboothibernate.entities.Employee;
import br.com.uanderson.springboothibernate.repository.EmployeeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class SpringbootHibernateApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootHibernateApplication.class, args);

        EmployeeRepository employeeRepository = context.getBean(EmployeeRepository.class);

        //Employee employee0 = new Employee("Employee 1", 25, "employee1@gmail.com");
        //Employee employee1 = new Employee("Employee 2", 32, "employee2@gmail.com");

        List<Employee> employeeList = new ArrayList<>();
        Random random = new Random();

//        for (int i = 0; i < 50_000; i++) {
//            // Gerar valores aleatórios
//            String name = String.format("Employee %d", i + 1);  // Nome único para cada Employee
//            int age = random.nextInt(43) + 18;  // Idade entre 18 e 60 anos
//            String email = String.format("employee%d@gmail.com", i + 1);  // Email único para cada Employee
//
//            // Criar o Employee com os valores gerados
//            Employee employeeToInsert = new Employee(name, age, email);
//            employeeList.add(employeeToInsert);
//        }

        //employeeRepository.saveAll(employeeList);

        // Exemplo com a dependecencia do Spring Data JPA
        //employeeRepository.save(employee0);
        //employeeRepository.save(employee1);

        System.out.println("Saved employees 1: " + employeeRepository.findAll());

        //Exemplo com Hibernate puro, conforme as aulas anteriores
        EmployeeDAO employeeDAO = context.getBean(EmployeeDAO.class);
        List<EmployeeDTO> allDTO = employeeDAO.findAllDTO();
        List<Employee> all = employeeDAO.findAll();
        List<Employee> allByJpa = employeeDAO.findAllByJpa();


        //System.out.println("Saved employees findAll(): " + employeeDAO.findAll());

        //System.out.println("Saved employees findAllDTO(): " + employeeDAO.findAllDTO());



    }//main

}//class
/*
     ConfigurableApplicationContext context = SpringApplication.run(SpringbootHibernateApplication.class, args);

      EmployeeRepository employeeRepository = context.getBean(EmployeeRepository.class);

1. SpringApplication.run(): Esse método é responsável por iniciar a aplicação Spring Boot. Ele faz o seguinte:
    Inicializa o contexto de aplicação Spring, que é o contêiner central onde os beans (componentes gerenciados pelo Spring)
    são registrados e gerenciados.Configura e lança a aplicação, carregando todas as configurações, beans, e recursos necessários.
    O retorno desse método é um objeto do tipo ConfigurableApplicationContext, que representa o contexto da aplicação Spring em execução.
    Esse contexto permite acessar e gerenciar os beans durante a execução da aplicação.

2. context.getBean(EmployeeRepository.class): Esse método recupera um bean do contexto da aplicação. Neste caso:
    Ele está buscando um bean do tipo EmployeeRepository, que deve ser um componente Spring gerenciado (como um @Repository).
    O Spring cria instâncias dos beans definidos e os gerencia. getBean() permite que você recupere um bean específico,
    geralmente para usá-lo em algum processamento.

Resumo:
Este código está iniciando a aplicação Spring Boot e, em seguida, recuperando o bean EmployeeRepository
do contexto de aplicação para que ele possa ser usado. O EmployeeRepository é provavelmente uma interface
que estende um repositório do Spring Data JPA, permitindo interações com o banco de dados.

 */