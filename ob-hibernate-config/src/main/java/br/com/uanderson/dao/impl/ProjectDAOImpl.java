package br.com.uanderson.dao.impl;

import br.com.uanderson.dao.ProjectDAO;
import br.com.uanderson.entities.Project;
import br.com.uanderson.util.HibernateUtil;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {
    @Override
    public List<Project> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        //Consulta HQL
        Query<Project> query = session.createQuery("from Project", Project.class);
        List<Project> directions = query.list();

        session.close();

        return directions;
    }

    @Override
    public Project findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Project project = session.find(Project.class, id);

        session.close();

        return project;
    }


    /*
        E interessante garantir a idepomtencia dos dados em operações que alteram
        o estado do banco de dados por isso estamos usando o session.beginTransaction()
        em conjunto com o rollback() em casos de erro durante o processo de uma transação.
        Pois lembre-se uma transação deve ter: COMEÇO, MEIO, FIM. É tudo ou nada.
     */

    @Override
    public Project create(Project project) {
        //Poderia usar o try-with-resources para lidar com o fechamento da session também
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.persist(project);

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro

        } finally {
            session.close();
        }

        return project;
    }

    @Override
    public Project update(Project project) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            //session.update(project) - @Deprecated(since = "6.0")
            session.merge(project);//Usar merge() no lugar de update(). (ATUALIZA E INSERE REGISTROS)

            session.getTransaction().commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();//desfaz a transação caso ocorra erro
        } finally {
            session.close();
        }

        return project;
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Project project = findById(id);

            session.remove(project);

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
