package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * класс HibernatePostRepository реализует интерфейс PostRepository
 * класс реализует хранилище объявлений в базе данных
 */
@Repository
@ThreadSafe
@AllArgsConstructor
public class HibernatePostRepository implements  PostRepository {

    private static final String DELETE = "delete Post where id = :fID";
    private static final String FIND_ALL =
            "select distinct p from Post as p "
                    + "join fetch p.car as cr join fetch cr.engine "
                    + "join fetch p.user ";

    private static final String FIND_BY_ID =
            "select distinct p from Post as p "
                    + "join fetch p.user "
                    + "join fetch p.priceHistoryList "
                    + "join fetch p.participates "
                    + "join fetch p.car"
                    + "where p.id = :fID";
    private static final String FIND_LAST_DAY =
            "select distinct p from Post as p "
                    + "join fetch p.user "
                    + "join fetch p.car "
                    + "where p.created between :fLast and :fNow";
    private static final String FIND_WITH_PHOTO =
            "select distinct p from Post as p "
                    + "join fetch p.user "
                    + "join fetch p.car "
                    + "where p.photo is not null";
    private static final String FIND_WITH_CAR =
            "select distinct p from Post as p "
                    + "join fetch p.user "
                    + "join fetch p.car "
                    + "where p.car in (from Car where name =:fCarName)";
    private static final Logger LOG = LoggerFactory
            .getLogger(HibernateCarRepository.class.getName());
    private CRUDRepository crudRepository;

    /**
     * добавляет объявление в базу данных
     * @param post - объявление
     * @return - Optional с добавленной машиной
     */
    @Override
    public Optional<Post> add(Post post) {
        Optional<Post> rsl = Optional.empty();
        try {
            crudRepository.optional(session -> session.persist(post));
            rsl = Optional.of(post);
        } catch (Exception e) {
            LOG.error("Ошибка добавления нового объявления" + e);
        }
        return rsl;
    }

    /**
     * даляет объявление из базы данных по id
     * @param id - индификатор
     * @return - результат операции
     */
    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(DELETE, Map.of("fID", id));
        } catch (Exception e) {
            LOG.error("Ошибка удаления объявление" + e);
        }
        return rsl;
    }

    /**
     * обновляет объявление в базе данных
     * @param post - объявление
     * @return - результат операции
     */
    @Override
    public boolean update(Post post) {
        boolean rsl = false;
        try {
            rsl = crudRepository.total(session -> session.merge(post).equals(post));
        } catch (Exception e) {
            LOG.error("Ошибка обновления машины" + e);
        }
        return rsl;
    }

    /**
     * находит все объявления в базе данных
     * @return - список объявлений
     */
    @Override
    public List<Post> findAll() {
        return crudRepository.list(FIND_ALL, Post.class);
    }

    /**
     * находит объявление в базе данных по id
     * @param id - индификатор
     * @return - Optional с объявлением с id
     */
    @Override
    public Optional<Post> findById(int id) {
        return crudRepository.optional(FIND_BY_ID, Post.class, Map.of("fID", id));
    }

    /**
     * находит объявления за последний день
     * @return - список объявлений
     */
    @Override
    public List<Post> findLastDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
        return crudRepository.list(FIND_LAST_DAY, Post.class, Map.of("fLast", lastDay, "fNow", now));
    }

    /**
     * находит объявления с фото
     * @return - список объявлений
     */
    @Override
    public List<Post> findWithPhoto() {
        return crudRepository.list(FIND_WITH_PHOTO, Post.class);
    }

    /**
     * находит объявления с машинами марки carName
     * @param carName - название машины
     * @return - список объявлений
     */
    @Override
    public List<Post> findWithCar(String carName) {
        return crudRepository.list(FIND_WITH_CAR, Post.class, Map.of("fCarName", carName));
    }
}
