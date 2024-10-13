package br.com.uanderson.dao;

import br.com.uanderson.entities.User;

import java.util.List;

public interface UserDAO {
    List<User> findAllActive();
}
