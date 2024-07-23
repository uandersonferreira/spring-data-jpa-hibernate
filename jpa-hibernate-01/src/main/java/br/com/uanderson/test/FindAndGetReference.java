package br.com.uanderson.test;


import br.com.uanderson.config.MyPersistenceUnitInfo;
import br.com.uanderson.entities.City;
import br.com.uanderson.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;

public class FindAndGetReference {
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

                //Find - 2 queries
                Student student1 = new Student();
                student1.setId(2L);
                student1.setName("Pedro");

                //Ao utilizar o find() ele irá carregar tudo de imediato, gerando uma nova consulta.
                student1.setCity(entityManager.find(City.class, 1L));

                entityManager.persist(student1);

                //getReference() - 1 query
                Student student2 = new Student();
                student2.setId(3L);
                student2.setName("Maria");

                //Por retornar um 'proxy' não irá executar a consulta para recuperar a cidade, pois não estamos chamando,
                //a mesma, portanto, não gera nova uma query. Melhor para o desempenho.
                student2.setCity(entityManager.getReference(City.class, 1L));

                entityManager.persist(student2);

                // Confirmar a transação (salvar as alterações no banco de dados)
                entityManager.getTransaction().commit();

                //OBS: LER O README, POIS ESTÁ MAIS DETALHADO :)
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