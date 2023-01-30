package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * класс HibernateEngineRepository реализует интерфейс EngineRepository
 * класс реализует хранилилще двигателей в базе данных
 */
@Repository
@ThreadSafe
@AllArgsConstructor
public class HibernateEngineRepository implements EngineRepository {

    private static final String DELETE = "delete Engine where id = :fID";
    private static final String FIND_ALL = "from Engine";
    private static final String FIND_BY_ID = "from Engine where id = :fID";
    private static final Logger LOG = LoggerFactory
            .getLogger(HibernateEngineRepository.class.getName());
    private CRUDRepository crudRepository;

    /**
     * добавляет двигатель в базу данных
     * @param engine - двигатель
     * @return - Optional с добавленным двигателем
     */
    @Override
    public Optional<Engine> add(Engine engine) {
        Optional<Engine> rsl = Optional.empty();
        try {
            crudRepository.optional(session -> session.persist(engine));
            rsl = Optional.of(engine);
        } catch (Exception e) {
            LOG.error("Ошибка добавления нового двигателя" + e);
        }
        return rsl;
    }

    /**
     * удаляет двигатель из базы данных по id
     * @param id - индификатор
     * @return - результат операции
     */
    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(DELETE, Map.of("fID", id));
        } catch (Exception e) {
            LOG.error("Ошибка удаления двигателя" + e);
        }
        return rsl;
    }

    /**
     * обновляет двигатель в базе данных
     * @param engine - двигатель
     * @return - результат операции
     */
    @Override
    public boolean update(Engine engine) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(session -> session.merge(engine).equals(engine));
        } catch (Exception e) {
            LOG.error("Ошибка обновления двигателя" + e);
        }
        return rsl;
    }

    /**
     * находит все двигатели в базе данных
     * @return список двигателей
     */
    @Override
    public List<Engine> findAll() {
        return crudRepository.list(FIND_ALL, Engine.class);
    }

    /**
     * находит двигатель в базе данных по id
     * @param id - индификатор
     * @return - Oprional с найденным двигателем
     */
    @Override
    public Optional<Engine> findById(int id) {
        return crudRepository.optional(FIND_BY_ID, Engine.class, Map.of("fID", id));
    }
}
