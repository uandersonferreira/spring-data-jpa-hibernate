package br.com.uanderson;

import br.com.uanderson.config.MyPersistenceUnitInfo;
import br.com.uanderson.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class EntidadesApplication2 {
    public static void main(String[] args) {
        //Define as propriedades do hibernate
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "true"); // Mostrar as queries SQL geradas pelo Hibernate
        properties.put("hibernate.format_sql", "true"); // Mostrar as queries SQL formatadas
        properties.put("hibernate.hbm2ddl.auto", "none");

        // Criar a fábrica de EntityManager (EntityManagerFactory)
        try (EntityManagerFactory entityManagerFactory =
                     new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                             new MyPersistenceUnitInfo(), properties)) {

            /* EntityManager -> Persistence Context */
            // Criar o EntityManager que gerencia o contexto de persistência
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

                // Iniciar uma transação
                entityManager.getTransaction().begin();

                //Obtendo uma instancia de um impl de session
                Session session = entityManager.unwrap(Session.class);

                Student student = entityManager.find(Student.class, 1L);

                System.out.println(student);
                System.out.println("Age: " + student.getAge());

                // Confirmar a transação (salvar as alterações no banco de dados)
                entityManager.getTransaction().commit();
            }
        }

    }//main
}//class
