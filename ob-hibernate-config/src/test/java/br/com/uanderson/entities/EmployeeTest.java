package br.com.uanderson.entities;

import br.com.uanderson.dao.EmployeeDAOImpl;
import br.com.uanderson.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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

    @Test
    void nickNamesTest() {
        Employee employee = new Employee(
                "John JUnit nicknames",
                "Doe",
                "johndoeteste@example.com",
                30,
                5000.0,
                true,
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now()
        );

        //Opção 1
//        ArrayList<String> nickNames = new ArrayList<>();
//        nickNames.add("nickname1");
//        nickNames.add("nickname2");
//        nickNames.add("nickname3");
//        employee.setNicknames(nickNames);

        //Opção 2
        employee.getNickNames().add("nickname1");
        employee.getNickNames().add("nickname2");
        employee.getNickNames().add("nickname3");

        employee.getPostalCode().add(45100121);
        employee.getPostalCode().add(21212121);
        employee.getPostalCode().add(36969852);

        employee.getCreditCards().add("123-123-123-1234");
        employee.getCreditCards().add("123-123-123-7892");

        employee.getPhones().put("654321123", "Claro");
        employee.getPhones().put("6557777876", "Tim");

        for (Map.Entry<String, String> phone : employee.getPhones().entrySet()) {
            System.out.print("Num telefone: " + phone.getKey() + " Operador: " +  phone.getValue());
        }

        employee.setCategory(EmployeeCategory.JUNIOR);

        EmployeeDAOImpl dao = new EmployeeDAOImpl();
        dao.create(employee);

        System.out.println(employee);
    }
}//class