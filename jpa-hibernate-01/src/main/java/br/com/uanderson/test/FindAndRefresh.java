package br.com.uanderson.test;


import br.com.uanderson.config.MyPersistenceUnitInfo;
import br.com.uanderson.entities.City;
import br.com.uanderson.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;

public class FindAndRefresh {
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

                // Buscar uma entidade Student do banco de dados - EQUIVALE AO SELECT DO SQL
                Student student = entityManager.find(Student.class, 1L);

                // Alterar o nome da entidade Student
                student.setName("Uanderson Oliveira 2");

                //O entityManager Usar o método refresh para sincronizar a entidade com o estado do banco de dados, então qualquer alteração
                //feita anteriormente é descartada pelo method, pois ele pega os dados mais atualziados já persistidos no banco
                //de dados.OBS. ASSIM COMO O FIND ELE EXECUTA UM SELECT DO SQL, JÁ QUE RETORNA OS DADOS SALVOS NO MESMO.
                entityManager.refresh(student);

                // Imprimir o estado da entidade após o refresh (com os dados do banco de dados e não com os das alterações)
                System.out.println(student);

                // Confirmar a transação (salvar as alterações no banco de dados)
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