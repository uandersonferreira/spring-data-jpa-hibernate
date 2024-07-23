package br.com.uanderson.test;


import br.com.uanderson.config.MyPersistenceUnitInfo;
import br.com.uanderson.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;

public class DetachAndFlush {
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

                // Encontrar a entidade Student
                Student student = entityManager.find(Student.class, 1L);

                // Modificar o nome da entidade
                student.setName("Uanderson");

                // Sincronizar as alterações pendentes no banco de dados, persistindo as alterações.
                //O flush() é chamado antes do detach(), forçando a sincronização das alterações
                //pendentes com o banco de dados. A modificação é persistida no banco de dados
                // mesmo após o detach().
                entityManager.flush();

                // Desanexar a entidade do contexto de persistência, não persiste as alterações feitas anteriormente.
                //A entidade é desanexada antes do commit, portanto,
                //nenhuma alteração é persistida no banco de dados. (num cenário sem o flush())
                entityManager.detach(student);

                // Confirmar a transação (salvar as alterações no banco de dados)
                entityManager.getTransaction().commit();

                // A entidade student é impressa, e a alteração foi salva no banco de dados
                System.out.println(student);
            }
        }

    }//main
}//class

/**
 * Comportamento somente com o detach:
 * <p>
 * 1. O entityManager.find(Student.class, 1L) carrega a entidade Student com id 1 do banco de dados e a coloca no contexto de persistência.
 * 2. A propriedade name do student é modificada para "Uanderson".
 * 3. O entityManager.detach(student) desanexa a entidade student do contexto de persistência.
 * 4. Quando a transação é confirmada (commit), a alteração do nome não é persistida no banco de dados porque a entidade foi desanexada antes da confirmação.
 * <p>
 * Comportamento com o flush:
 * <p>
 * 1. O entityManager.find(Student.class, 1L) carrega a entidade Student com id 1 do
 * banco de dados e a coloca no contexto de persistência.
 * 2. A propriedade name do student é modificada para "Uanderson".
 * 3. O entityManager.flush() sincroniza as alterações pendentes no banco de dados.
 * Isso força a execução de qualquer SQL necessário para refletir as mudanças
 * feitas até o momento.
 * 4. O entityManager.detach(student) desanexa a entidade student do contexto de persistência.
 * 5. Quando a transação é confirmada (commit), a alteração do nome já foi persistida no
 * banco de dados pelo flush(), então a confirmação não tem efeito adicional no banco de dados.
 * <p>
 * <p>
 * <p>
 * <p>
 * /*
 * Resumo do Fluxo
 * 1. Configuração: Configura o EntityManagerFactory com a unidade de persistência especificada no persistence.xml.
 * 2. Criação do EntityManager: Cria um EntityManager para gerenciar o contexto de persistência.
 * 3. Transação: Inicia uma transação.
 * 4. Manipulação da Entidade: Cria uma nova entidade Student, configura seus atributos e a persiste no banco de dados.
 * 5. Commit: Confirma a transação para salvar as mudanças no banco de dados.
 * 6. Fechamento: Fecha os recursos (EntityManager e EntityManagerFactory).
 * <p>
 * <p>
 * pelo xml:
 * EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit")
 */