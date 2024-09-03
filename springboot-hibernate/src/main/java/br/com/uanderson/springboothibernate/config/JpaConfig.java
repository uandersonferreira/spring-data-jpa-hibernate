package br.com.uanderson.springboothibernate.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {

    private final EntityManagerFactory entityManagerFactory; // JPA

    public JpaConfig(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Session getSession(){
        // Convertendo EntityManagerFactory para SessionFactory
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        // Retorna uma sessão aberta para ser usada pelo Hibernate
        return sessionFactory.openSession(); // session de Hibernate
    }

}//class
