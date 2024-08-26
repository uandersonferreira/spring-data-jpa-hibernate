package br.com.uanderson.dao.impl;

import br.com.uanderson.dao.DirectionDAO;
import br.com.uanderson.entities.Direction;
import br.com.uanderson.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import jakarta.persistence.*;

import java.util.List;

public class DirectionDAOImpl implements DirectionDAO {
    @Override
    public List<Direction> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //Consulta HQL
        Query<Direction> query = session.createQuery("from Direction", Direction.class);
        List<Direction> directions = query.list();

        session.close();

        return directions;
    }

    @Override
    public Direction findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Direction direction = session.find(Direction.class, id);

        session.close();

        return direction;
    }


    /*
        E interessante garantir a idepomtencia dos dados em operações que alteram
        o estado do banco de dados por isso estamos usando o session.beginTransaction()
        em conjunto com o rollback() em casos de erro durante o processo de uma transação.
        Pois lembre-se uma transação deve ter: COMEÇO, MEIO, FIM. É tudo ou nada.
     */

    @Override
    public Direction create(Direction direction) {
        //Poderia usar o try-with-resources para lidar com o fechamento da session também
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.persist(direction);

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro

        } finally {
            session.close();
        }

        return direction;
    }

    @Override
    public Direction update(Direction direction) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            //session.update(direction) - @Deprecated(since = "6.0")
            session.merge(direction);//Usar merge() no lugar de update(). (ATUALIZA E INSERE REGISTROS)

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro
        } finally {
            session.close();
        }

        return direction;
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Direction direction = findById(id);

            session.remove(direction);

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
