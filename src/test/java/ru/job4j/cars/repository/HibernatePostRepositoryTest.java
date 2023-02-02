package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HibernatePostRepositoryTest {

    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();

    private final CRUDRepository crudRepository = new HibernateCRUDRepository(sf);
    private final PostRepository postRepository = new HibernatePostRepository(crudRepository);
    private final UserRepository userRepository = new HibernateUserRepository(crudRepository);
    private final EngineRepository engineRepository = new HibernateEngineRepository(crudRepository);
    private final CarRepository carRepository = new HibernateCarRepository(crudRepository);

    @AfterEach
    public void clear() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete Post").executeUpdate();
        session.createQuery("delete User").executeUpdate();
        session.createQuery("delete Car").executeUpdate();
        session.createQuery("delete Engine").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenAddPost() {
        User user = new User(0, "login", "pass");
        userRepository.create(user);
        Post post = new Post();
        post.setDescription("desc");
        post.setUser(user);
        Optional<Post> rsl = postRepository.add(post);
        assertThat(rsl.get().getDescription()).isEqualTo("desc");
    }

    @Test
    public void whenUpdatePost() {
        User user = new User(0, "login", "pass");
        userRepository.create(user);
        Post post = new Post();
        post.setDescription("desc");
        post.setUser(user);
        postRepository.add(post);

        Post post1 = new Post();
        post1.setId(post.getId());
        post1.setDescription("description");
        post1.setUser(user);
        boolean rsl = postRepository.update(post1);
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenDeletePost() {
        User user = new User(0, "login", "pass");
        userRepository.create(user);
        Post post = new Post();
        post.setDescription("desc");
        post.setUser(user);
        postRepository.add(post);

        boolean rsl = postRepository.delete(post.getId());
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenFindAll() {
        User user = new User(0, "login", "pass");
        userRepository.create(user);
        Post post = new Post();
        post.setDescription("desc");
        post.setUser(user);
        postRepository.add(post);

        User user2 = new User(0, "login2", "pass2");
        userRepository.create(user2);
        Post post2 = new Post();
        post2.setDescription("description");
        post2.setUser(user2);
        postRepository.add(post2);

        List<Post> expect = List.of(post, post2);
        List<Post> rsl = postRepository.findAll();
        assertThat(rsl).isEqualTo(expect);
    }

    @Test
    public void whenFindLastDay() {
        LocalDateTime today = LocalDateTime.now()
                .minus(1, ChronoUnit.HOURS);
        LocalDateTime yesterday = LocalDateTime.now()
                .minus(2, ChronoUnit.DAYS);

        User user = new User(0, "login", "pass");
        userRepository.create(user);
        Post post = new Post();
        post.setDescription("desc");
        post.setCreated(today);
        post.setUser(user);
        postRepository.add(post);

        User user2 = new User(0, "login2", "pass2");
        userRepository.create(user2);
        Post post2 = new Post();
        post2.setDescription("description");
        post2.setCreated(yesterday);
        post2.setUser(user2);
        postRepository.add(post2);

        List<Post> expect = List.of(post);
        List<Post> rsl = postRepository.findLastDay();
        assertThat(rsl).isEqualTo(expect);
    }

    @Test
    public void whenFindWithPhoto() {
        User user = new User(0, "login", "pass");
        userRepository.create(user);
        Post post = new Post();
        post.setDescription("desc");
        post.setUser(user);
        post.setPhoto(new byte[]{1, 1, 1, 1});
        postRepository.add(post);

        User user2 = new User(0, "login2", "pass2");
        userRepository.create(user2);
        Post post2 = new Post();
        post2.setDescription("description");
        post2.setUser(user2);
        postRepository.add(post2);

        List<Post> expect = List.of(post);
        List<Post> rsl = postRepository.findWithPhoto();
        assertThat(rsl).isEqualTo(expect);
    }

    @Test
    public void whenFindWithCar() {
        User user = new User(0, "login", "pass");
        userRepository.create(user);
        Post post = new Post();
        post.setDescription("desc");
        post.setUser(user);
        Engine engine = new Engine(0, "engine");
        engineRepository.add(engine);
        Car car = new Car(0, "car", engine, null);
        carRepository.add(car);
        post.setCar(car);
        postRepository.add(post);

        User user2 = new User(0, "login2", "pass2");
        userRepository.create(user2);
        Post post2 = new Post();
        post2.setDescription("description");
        post2.setUser(user2);
        Car car2 = new Car(0, "car2", engine, null);
        carRepository.add(car);
        post.setCar(car2);
        postRepository.add(post2);

        List<Post> expect = List.of(post);
        List<Post> rsl = postRepository.findWithCar("car");
        assertThat(rsl).isEqualTo(expect);
    }
}