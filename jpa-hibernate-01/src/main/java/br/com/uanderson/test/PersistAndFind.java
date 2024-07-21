package br.com.uanderson.test;


import br.com.uanderson.config.MyPersistenceUnitInfo;
import br.com.uanderson.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;

public class PersistAndFind {
    public static void main(String[] args) {
        //Define as propriedades do hibernate
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.show_sql", "true"); // Mostrar as queries SQL geradas pelo Hibernate
        properties.put("hibernate.format_sql", "true"); // Mostrar as queries SQL formatadas

        // Criar a fábrica de EntityManager (EntityManagerFactory)
        try (EntityManagerFactory entityManagerFactory =
                     new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                             new MyPersistenceUnitInfo(), properties)) {

            /* EntityManager -> Persistence Context */
            // Criar o EntityManager que gerencia o contexto de persistência
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

                // Iniciar uma transação
                entityManager.getTransaction().begin();

//                // Criar uma nova instância da entidade Student (NEW)
//                Student student1 = new Student();
//
//                // Definir o ID do estudante
//                student1.setId(1L);
//
//                // Definir o nome do estudante
//                student1.setName("Uanderson Oliveira");
//
//                // Persistir/Passar a existir no contexto de persistẽncia pois queremos (salvar)
//                // a entidade student1 no banco de dados
                //EQUIVALE AO INSERT DO SQL
//                entityManager.persist(student1);


                //find - EQUIVALE AO SELECT COM WHERE DO SQL, Recupera o registro do database e insere no contexto de persistência
                Student student1 = entityManager.find(Student.class, 1L);

                //Mesmo modificando, aqui os objetos continuam sendo o mesmo, pois tem o mesmo id
                //Essa mudança, somente será persistida, quando a transação terminar (IMPORTANTE)
                // TRANSACTION - COMEÇO | MEIO | FIM - Tudo ou nada - Principio da Atomicidade.
                student1.setName("NOVO NOME");


                //Por estamos buscando um registro com o id, que já existe no contexto de persitência
                //Irá somente nos retornar esse registro, Não irá fazer um novo SELECT COM WHERE DO SQL
                Student student2 = entityManager.find(Student.class, 1L);

                //Mesmo tendo alterado o nome em cima, irá trazer o último registro salvo, antes dessa transação ser finalizada.
                System.out.println(student1);

                //Ambas referências apontam para o mesmo object em memória;
                System.out.println("isTrue: " + (student1 == student2));

                //isso irá gerar um UPDATE DO SQL, pois a transação ainda não foi finalizada/comitada
                //student1.setName("Uanderson Oliveira 2");

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