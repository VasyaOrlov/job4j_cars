package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HibernateUserRepositoryTest {

    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();

    private final CRUDRepository crudRepository = new HibernateCRUDRepository(sf);
    private final UserRepository userRepository = new HibernateUserRepository(crudRepository);

    @AfterEach
    public void clear() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenCreateUser() {
        User user = new User(0, "login", "password", "Africa/Abidjan");
        Optional<User> rsl = userRepository.create(user);
        String rslLogin = rsl.isEmpty() ? null : rsl.get().getLogin();
        assertThat(rslLogin).isEqualTo("login");

        User user2 = new User(user.getId(), "login2", "pass2", null);
        Optional<User> rsl2 = userRepository.create(user2);
        assertThat(rsl2.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteUser() {
        User user = new User(0, "login", "password", null);
        userRepository.create(user);
        boolean rsl = userRepository.delete(user.getId());
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenFindAllOrderById() {
        User user1 = new User(0, "login1", "password1", null);
        User user2 = new User(0, "login2", "password2", null);
        User user3 = new User(0, "login3", "password3", null);

        userRepository.create(user3);
        userRepository.create(user1);
        userRepository.create(user2);

        List<User> expect = List.of(user3, user1, user2);
        List<User> rsl = userRepository.findAllOrderById();
        assertThat(rsl).isEqualTo(expect);
    }

    @Test
    public void whenFindById() {
        User user = new User(0, "login", "password", null);
        userRepository.create(user);
        Optional<User> rsl = userRepository.findById(user.getId());
        assertThat(rsl.get().getLogin()).isEqualTo("login");
    }

    @Test
    public void whenFindByLikeLogin() {
        User user1 = new User(0, "log3in1", "password1", null);
        User user2 = new User(0, "login2", "password2", null);
        User user3 = new User(0, "log3in2", "password3", null);

        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);

        List<User> expect = List.of(user1, user3);
        List<User> rsl = userRepository.findByLikeLogin("g3i");
        assertThat(rsl).isEqualTo(expect);
    }

    @Test
    public void whenFindByLogin() {
        User user1 = new User(0, "log3in1", "password1", null);
        User user2 = new User(0, "login2", "password2", null);
        User user3 = new User(0, "log3in2", "password3", null);

        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);

        Optional<User> rsl = userRepository.findByLogin("login2");
        assertThat(rsl.get().getLogin()).isEqualTo("login2");
    }
}