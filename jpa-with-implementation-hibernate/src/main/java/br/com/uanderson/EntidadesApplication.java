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

public class EntidadesApplication {
    public static void main(String[] args) {
        //Define as propriedades do hibernate
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "true"); // Mostrar as queries SQL geradas pelo Hibernate
        properties.put("hibernate.format_sql", "true"); // Mostrar as queries SQL formatadas
        properties.put("hibernate.hbm2ddl.auto", "create");

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

                Student student = new Student();
                student.setName("Uanderson");
                student.setEmail("uanderson@gmail.com");
                student.setPassword("123");
                student.setBirthDate(LocalDate.of(2002, Month.AUGUST, 13));

                Student student2 = new Student();
                student2.setName("Pedro");
                student2.setEmail("pedro@gmail.com");
                student.setPassword("1234");
                student2.setBirthDate(LocalDate.of(2000, Month.SEPTEMBER, 13));


                entityManager.persist(student);
                entityManager.persist(student2);

                entityManager.flush();
                // Recuperar o objeto pelo ID natural (NaturalId) usando Session
               // Student student3 = session.bySimpleNaturalId(Student.class).load("pedro@gmail.com");




                // Confirmar a transação (salvar as alterações no banco de dados)
                entityManager.getTransaction().commit();
            }
        }

    }//main
}//class
