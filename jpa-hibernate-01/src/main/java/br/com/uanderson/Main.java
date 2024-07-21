package br.com.uanderson;


import br.com.uanderson.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {

        // Criar a fábrica de EntityManager (EntityManagerFactory)
        try (EntityManagerFactory entityManagerFactory =
                     Persistence.createEntityManagerFactory("my-persistence-unit")) {

            /* EntityManager -> Persistence Context */
            // Criar o EntityManager que gerencia o contexto de persistência
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

                // Iniciar uma transação
                entityManager.getTransaction().begin();

                // Criar uma nova instância da entidade Student
                Student student1 = new Student();

                // Definir o ID do estudante
                student1.setId(1L);

                // Definir o nome do estudante
                student1.setName("Uanderson Oliveira");

                // Persistir/Passar a existir no contexto de persistẽncia pois queremos (salvar)
                // a entidade student1 no banco de dados
                entityManager.persist(student1);

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
 */