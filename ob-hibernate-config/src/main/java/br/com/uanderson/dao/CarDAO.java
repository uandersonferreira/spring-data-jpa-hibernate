package br.com.uanderson.dao;

import br.com.uanderson.entities.Car;

import java.util.List;

public interface CarDAO {

    List<Car> findAll();
    Car findById(Long id);
    Car create(Car car);
    Car update(Car car);
    boolean deleteById(Long id);
}
