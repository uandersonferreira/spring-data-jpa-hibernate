package br.com.uanderson.entities;

import br.com.uanderson.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

class EmployeeTest {

    @Test
    void createTablesTest() {
        Employee employee1 = new Employee(
                "John",
                "Doe",
                "johndoe@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now()
        );

        Employee employee2 = new Employee(
                "Jane",
                "Smith",
                "janesmith@example.com",
                28,
                4500.0,
                false,
                LocalDate.of(1982, 12, 25),
                LocalDateTime.now()
        );

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.persist(employee1);//save-@Deprecated(since = "6.0") usar persist
        session.persist(employee2);//save-@Deprecated(since = "6.0")

        session.getTransaction().commit();

        session.close();
        sessionFactory.close();
        HibernateUtil.shutdown();

    }//method


}//class