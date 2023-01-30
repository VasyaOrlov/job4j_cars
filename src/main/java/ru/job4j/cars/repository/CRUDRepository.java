package ru.job4j.cars.repository;

import org.hibernate.Session;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * CRUDRepository - интерфейс инкапсуляции запроса
 */
public interface CRUDRepository {

    <T> Optional<T> optional(Consumer<Session> con);
    <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> map);
    boolean total(String query, Map<String, Object> map);
    boolean total(Function<Session, Boolean> function);
    <T> List<T> list(String query, Class<T> cl);
    <T> List<T> list(String query, Class<T> cl, Map<String, Object> map);
}
