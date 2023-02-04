package ru.job4j.cars.repository;

import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository - интерфейс хранилища пользователей
 */
public interface UserRepository {

    Optional<User> create(User user);
    boolean update(User user);
    boolean delete(int userId);
    List<User> findAllOrderById();
    Optional<User> findById(int id);
    List<User> findByLikeLogin(String key);
    Optional<User> findByLogin(String login);
    Optional<User> findByLoginPass(String login, String pass);
}
