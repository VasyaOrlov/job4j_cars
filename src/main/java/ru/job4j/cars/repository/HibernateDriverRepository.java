package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Driver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * класс HibernateDriverRepository реализует интерфейс DriverRepository
 * класс реализует хранилище водителей в базе данных
 */
@Repository
@ThreadSafe
@AllArgsConstructor
public class HibernateDriverRepository implements DriverRepository {

    private static final String DELETE = "delete Driver where id = :fID";
    private static final String FIND_ALL =
            "from Driver as d join fetch d.user";
    private static final String FIND_BY_ID =
            "from Driver as d join fetch d.user where d.id = :fID";
    private static final Logger LOG = LoggerFactory
            .getLogger(HibernateDriverRepository.class.getName());
    private CRUDRepository crudRepository;

    /**
     * добавляет водителя в базу данных
     * @param driver - водитель
     * @return - Optional  с добавленным водителем
     */
    @Override
    public Optional<Driver> add(Driver driver) {
        Optional<Driver> rsl = Optional.empty();
        try {
            crudRepository.optional(session -> session.persist(driver));
            rsl = Optional.of(driver);
        } catch (Exception e) {
            LOG.error("Ошибка добавления нового водителя" + e);
        }
        return rsl;
    }

    /**
     * удаляет водителя из базы данных по id
     * @param id - индификатор
     * @return - результат операции
     */
    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(DELETE, Map.of("fID", id));
        } catch (Exception e) {
            LOG.error("Ошибка удаления водителя" + e);
        }
        return rsl;
    }

    /**
     * обновляет водителя в базе данных
     * @param driver - водитель
     * @return - результат операции
     */
    @Override
    public boolean update(Driver driver) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(session -> session.merge(driver).equals(driver));
        } catch (Exception e) {
            LOG.error("Ошибка обновления водителя" + e);
        }
        return rsl;
    }

    /**
     * находит всех водителей в базе данных
     * @return - список водителей
     */
    @Override
    public List<Driver> findAll() {
        return crudRepository.list(FIND_ALL, Driver.class);
    }

    /**
     * находит водителя в базе данных по id
     * @param id - индификатор
     * @return - Optional  с найденным водителем
     */
    @Override
    public Optional<Driver> findById(int id) {
        return crudRepository.optional(FIND_BY_ID, Driver.class, Map.of("fID", id));
    }
}
