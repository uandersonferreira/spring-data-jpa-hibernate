package br.com.uanderson.test;


import br.com.uanderson.config.MyPersistenceUnitInfo;
import br.com.uanderson.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;

public class Merge {
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

                //MERGE
                Student student = new Student();

                student.setId(1L);
                student.setName("Uanderson merged");

                //A instância retornada pelo merge está no estado gerenciado. As mudanças feitas
                // nesta instância serão rastreadas pelo EntityManager e sincronizadas com o banco
                // de dados quando a transação for confirmada.
                Student studentMerged = entityManager.merge(student);
                //executa um find() (SELECT COM WHERE) e se encontrado o objeto as propriedades do objeto 'novo' são
                //copiadas para o objeto que foi encontrado pelo find(), logo o mesmo está no contexto de persistência
                //E o 'novo' não então será feito um uptade. Caso o find() não encontre irá salvar o 'novo' objeto.

                // Confirmar a transação (salvar as alterações no banco de dados)
                entityManager.getTransaction().commit();

                // A entidade student é impressa, e a alteração foi salva no banco de dados
                System.out.println(student);


                System.out.println(student == studentMerged);//return false, pois 'student' (object new) Está no estado 'NEW'
                //Não pertence ao contexto de persistência e o 'studentMerged' está no estado 'MANAGER' que pertence ao contexto
                //de persistência e que faz referência ao objeto salvo no banco de dados;

            }
        }

    }//main
}//class
