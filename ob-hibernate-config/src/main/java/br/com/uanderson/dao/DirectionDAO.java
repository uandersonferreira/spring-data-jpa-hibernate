package br.com.uanderson.dao;

import br.com.uanderson.entities.Direction;

import java.util.List;

public interface DirectionDAO {

    List<Direction> findAll();
    Direction findById(Long id);
    Direction create(Direction direction);
    Direction update(Direction direction);
    boolean deleteById(Long id);
}
