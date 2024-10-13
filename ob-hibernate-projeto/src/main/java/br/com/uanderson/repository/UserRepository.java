package br.com.uanderson.repository;

import br.com.uanderson.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository// Optional
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByDni(String dni);
    List<User> findUsersByBirthDateBetween(LocalDate startDate, LocalDate endDate);

}
