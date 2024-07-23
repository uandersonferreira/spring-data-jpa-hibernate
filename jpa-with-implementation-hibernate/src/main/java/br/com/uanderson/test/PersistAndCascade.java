package br.com.uanderson.test;


import br.com.uanderson.config.MyPersistenceUnitInfo;
import br.com.uanderson.entities.City;
import br.com.uanderson.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;

public class PersistAndCascade {
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

                //Persist ---- Cascade
                City california = new City();
                california.setId(1L);
                california.setName("California");

                Student student = new Student();
                student.setId(1L);
                student.setName("Uanderson Oliveira");
                student.setCity(california);

                //Irá persistir tanto a entidade student quanto a City
                entityManager.persist(student);
                //Irá inserir somente uma vez o mesmo objeto - Tudo abaixo é ignorado basicamente, se a entidade já estiver persistente/inserida
                //entityManager.persist(student);
                //entityManager.persist(student);


                // Confirmar a transação (salvar as alterações no banco de dados)
                //Sincronização das transações no contexto de persistência com o database(DBMS)
                entityManager.getTransaction().commit();
            }

        }

    }//main

}//class

/*
Resumo do Fluxo
1. Configuração: Configura o EntityManagerFactory com a unidade de persistência especificada no persistence.xml.
2. Criação do EntityManager: Cria um EntityManager para gerenciar o contexto de persistência.
3. Transação: Inicia uma transação.
4. Manipulação da Entidade: Cria uma nova entidade Student, configura seus atributos e a persiste no banco de dados.
5. Commit: Confirma a transação para salvar as mudanças no banco de dados.
6. Fechamento: Fecha os recursos (EntityManager e EntityManagerFactory).


pelo xml:
EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit")

 */