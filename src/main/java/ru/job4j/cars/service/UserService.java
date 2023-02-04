package ru.job4j.cars.service;

import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> create(User user);
    Optional<User> findByLoginPass(String login, String pass);
}
