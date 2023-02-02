package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private static final String FIND_ALL_ORDER_ID = "from User order by id";
    private static final String DELETE_ID = "delete User where id = :fid";
    private static final String  FIND_LOGIN_LIKE = "from User where login like :key";
    private static final String  FIND_BY_ID = "from User where id = :fid";
    private static final String  FIND_BY_LOGIN = "from User where login = :fLogin";
    private static final Logger LOG = LoggerFactory.getLogger(HibernateUserRepository.class.getName());
    private CRUDRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public Optional<User> create(User user) {
        Optional<User> rsl = Optional.empty();
        try {
            crudRepository.optional(session -> session.persist(user));
            rsl = Optional.of(user);
        } catch (Exception e) {
            LOG.error("Ошибка добавления нового пользователя" + e);
        }
        return rsl;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public boolean update(User user) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(session -> session.merge(user).equals(user));
        } catch (Exception e) {
            LOG.error("Ошибка обновления пользователя" + e);
        }
        return rsl;
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public boolean delete(int userId) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(DELETE_ID, Map.of("fid", userId));
        } catch (Exception e) {
            LOG.error("Ошибка удаления пользователя" + e);
        }
        return rsl;
    }

    /**
     * Список пользователей отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        return crudRepository.list(FIND_ALL_ORDER_ID, User.class);
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        Optional<User> rsl = Optional.empty();
        try {
            rsl = crudRepository.optional(FIND_BY_ID, User.class, Map.of("fid", id));
        } catch (Exception e) {
            LOG.error("Ошибка поиска пользователя по id" + e);
        }
        return rsl;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        return crudRepository.list(FIND_LOGIN_LIKE,
                User.class,
                Map.of("key", "%" + key + "%"));
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Optional<User> rsl = Optional.empty();
        try {
            rsl = crudRepository.optional(FIND_BY_LOGIN, User.class, Map.of("fLogin", login));
        } catch (Exception e) {
            LOG.error("Ошибка поиска пользователя по id" + e);
        }
        return rsl;
    }
}