package ru.job4j.cars.repository;

import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> add(Post post);
    boolean delete(int id);
    boolean update(Post post);
    List<Post> findAll();
    Optional<Post> findById(int id);
    List<Post> findLastDay();
    List<Post> findWithPhoto();
    List<Post> findWithCar(String carName);
}
