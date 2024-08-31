package br.com.uanderson.dao.impl;

import br.com.uanderson.dao.CarDAO;
import br.com.uanderson.entities.Car;
import br.com.uanderson.util.HibernateUtil;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CarDAOImpl implements CarDAO {
    @Override
    public List<Car> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //Consulta HQL
        Query<Car> query = session.createQuery("from Car", Car.class);
        List<Car> directions = query.list();

        session.close();

        return directions;
    }

    @Override
    public Car findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Car car = session.find(Car.class, id);

        session.close();

        return car;
    }


    /*
        E interessante garantir a idepomtencia dos dados em operações que alteram
        o estado do banco de dados por isso estamos usando o session.beginTransaction()
        em conjunto com o rollback() em casos de erro durante o processo de uma transação.
        Pois lembre-se uma transação deve ter: COMEÇO, MEIO, FIM. É tudo ou nada.
     */

    @Override
    public Car create(Car car) {
        //Poderia usar o try-with-resources para lidar com o fechamento da session também
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.persist(car);

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro

        } finally {
            session.close();
        }

        return car;
    }

    @Override
    public Car update(Car car) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            //session.update(car) - @Deprecated(since = "6.0")
            session.merge(car);//Usar merge() no lugar de update(). (ATUALIZA E INSERE REGISTROS)

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro
        } finally {
            session.close();
        }

        return car;
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Car car = findById(id);

            session.remove(car);

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
