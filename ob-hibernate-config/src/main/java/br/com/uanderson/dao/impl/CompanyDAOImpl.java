package br.com.uanderson.dao.impl;

import br.com.uanderson.dao.CompanyDAO;
import br.com.uanderson.entities.Company;
import br.com.uanderson.util.HibernateUtil;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CompanyDAOImpl implements CompanyDAO {
    @Override
    public List<Company> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //Consulta HQL
        Query<Company> query = session.createQuery("from Company", Company.class);
        List<Company> directions = query.list();

        session.close();

        return directions;
    }

    @Override
    public Company findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Company company = session.find(Company.class, id);

        session.close();

        return company;
    }


    /*
        E interessante garantir a idepomtencia dos dados em operações que alteram
        o estado do banco de dados por isso estamos usando o session.beginTransaction()
        em conjunto com o rollback() em casos de erro durante o processo de uma transação.
        Pois lembre-se uma transação deve ter: COMEÇO, MEIO, FIM. É tudo ou nada.
     */

    @Override
    public Company create(Company company) {
        //Poderia usar o try-with-resources para lidar com o fechamento da session também
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.persist(company);

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro

        } finally {
            session.close();
        }

        return company;
    }

    @Override
    public Company update(Company company) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            //session.update(company) - @Deprecated(since = "6.0")
            session.merge(company);//Usar merge() no lugar de update(). (ATUALIZA E INSERE REGISTROS)

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro
        } finally {
            session.close();
        }

        return company;
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Company company = findById(id);

            session.remove(company);

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro
            return false;
        } finally {
            session.close();
        }

        return true;
    }
}
