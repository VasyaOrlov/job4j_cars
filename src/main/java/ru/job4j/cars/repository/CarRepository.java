package ru.job4j.cars.repository;

import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Optional;

/**
 * CarRepository - интерфейс хранилища машин
 */
public interface CarRepository {
    Optional<Car> add(Car car);
    boolean delete(int id);
    boolean update(Car car);
    List<Car> findAll();
    Optional<Car> findById(int id);
}
