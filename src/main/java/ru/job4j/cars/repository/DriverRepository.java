package ru.job4j.cars.repository;

import ru.job4j.cars.model.Driver;

import java.util.List;
import java.util.Optional;

/**
 * DriverRepository - интерфейс хранилища водителей
 */
public interface DriverRepository {

    Optional<Driver> add(Driver driver);
    boolean delete(int id);
    boolean update(Driver driver);
    List<Driver> findAll();
    Optional<Driver> findById(int id);
}
