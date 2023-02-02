package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * класс HibernateCarRepository реализует интерфейс CarRepository
 * класс реализует хранилище машин в базе данных
 */
@Repository
@ThreadSafe
@AllArgsConstructor
public class HibernateCarRepository implements CarRepository {

    private static final String DELETE = "delete Car where id = :fID";
    private static final String FIND_ALL =
            "select distinct c from Car as c join fetch c.engine";
    private static final String FIND_BY_ID =
            "select distinct c from Car as c join fetch c.engine where c.id = :fID";
    private static final Logger LOG = LoggerFactory
            .getLogger(HibernateCarRepository.class.getName());
    private CRUDRepository crudRepository;

    /**
     * добавляет машину в базу данных
     * @param car - машина
     * @return - Optional с добавленной машиной
     */
    @Override
    public Optional<Car> add(Car car) {
        Optional<Car> rsl = Optional.empty();
        try {
            crudRepository.optional(session -> session.persist(car));
            rsl = Optional.of(car);
        } catch (Exception e) {
            LOG.error("Ошибка добавления новой машины" + e);
        }
        return rsl;
    }

    /**
     * даляет машину из базы данных по id
     * @param id - индификатор
     * @return - результат операции
     */
    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(DELETE, Map.of("fID", id));
        } catch (Exception e) {
            LOG.error("Ошибка удаления машины" + e);
        }
        return rsl;
    }

    /**
     * обновляет машину в базе данных
     * @param car - машина
     * @return - результат операции
     */
    @Override
    public boolean update(Car car) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(session -> session.merge(car).equals(car));
        } catch (Exception e) {
            LOG.error("Ошибка обновления машины" + e);
        }
        return rsl;
    }

    /**
     * находит все машины в базе данных
     * @return - список машин
     */
    @Override
    public List<Car> findAll() {
        return crudRepository.list(FIND_ALL, Car.class);
    }

    /**
     * находит машину в базе данных по id
     * @param id - индификатор
     * @return - Optional с машиной с id
     */
    @Override
    public Optional<Car> findById(int id) {
        return crudRepository.optional(FIND_BY_ID, Car.class, Map.of("fID", id));
    }
}
