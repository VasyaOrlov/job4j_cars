package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {

    private static final String FIND_ALL_ORDER_ID = "from User order by id";
    private static final String DELETE_ID = "delete User where id = :fid";
    private static final String  FIND_LOGIN_LIKE = "from User where login like :key";
    private static final String  FIND_BY_LOGIN = "from User where login = :fLogin";
    private static final String  UPDATE =
            "update User set login = :fLogin, password = :fPassword where id = :fId";
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.getTransaction().rollback();
            session.close();
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(UPDATE)
                    .setParameter("fLogin", user.getLogin())
                    .setParameter("fPassword", user.getPassword())
                    .setParameter("fId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.getTransaction().rollback();
            session.close();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(DELETE_ID)
                    .setParameter("fid", userId)
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.getTransaction().rollback();
            session.close();
        }
    }

    /**
     * Список пользователей отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<User> rsl = session.createQuery(FIND_ALL_ORDER_ID, User.class).list();
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Optional<User> rsl = Optional.ofNullable(session.get(User.class, id));
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        session.beginTransaction();
        List<User> rsl = session.createQuery(FIND_LOGIN_LIKE, User.class)
                .setParameter("key", "%" + key + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        session.beginTransaction();
        Optional<User> rsl = Optional.ofNullable(session.createQuery(FIND_BY_LOGIN, User.class)
                .setParameter("fLogin", login)
                .uniqueResult());
        session.getTransaction().commit();
        session.close();
        return rsl;
    }
}