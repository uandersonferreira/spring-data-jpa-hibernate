package br.com.uanderson.dao;

import br.com.uanderson.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllActive() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class); //Table User

        Predicate isActivePredicate = criteriaBuilder.isTrue(root.get("active"));

        criteriaQuery.select(root).where(isActivePredicate);

        List<User> users = entityManager.createQuery(criteriaQuery).getResultList();

        return users;
    }

    public User findUserByDni(String dni) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("dni"), dni));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    public List<User> findUsersByBirthDateBetween(LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(root)
                .where(criteriaBuilder.between(root.get("birthDate"), startDate, endDate));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}//clas
