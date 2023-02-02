package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Driver;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HibernateDriverRepositoryTest {

    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();

    private final CRUDRepository crudRepository = new HibernateCRUDRepository(sf);

    private final DriverRepository driverRepository = new HibernateDriverRepository(crudRepository);
    private final UserRepository userRepository = new HibernateUserRepository(crudRepository);

    @AfterEach
    public void clear() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete Driver").executeUpdate();
        session.createQuery("delete User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenAddDriver() {
        User user = new User(0, "login", "password");
        userRepository.create(user);
        Driver driver = new Driver(0, "водитель 1", user);
        Optional<Driver> rsl = driverRepository.add(driver);
        String rslName = rsl.isEmpty() ? null : rsl.get().getName();
        assertThat(rslName).isEqualTo("водитель 1");

        Driver driver1 = new Driver(driver.getId(), "водитель 2", user);
        Optional<Driver> rsl1 = driverRepository.add(driver1);
        assertThat(rsl1.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteDriver() {
        User user = new User(0, "login", "password");
        userRepository.create(user);
        Driver driver = new Driver(0, "водитель 1", user);
        driverRepository.add(driver);
        boolean rsl = driverRepository.delete(driver.getId());
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenUpdateDriver() {
        User user = new User(0, "login", "password");
        userRepository.create(user);
        Driver driver = new Driver(0, "водитель 1", user);
        driverRepository.add(driver);
        Driver driver2 = new Driver(driver.getId(), "водитель 2", user);
        boolean rsl = driverRepository.update(driver2);
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenFindAll() {
        User user = new User(0, "login", "password");
        userRepository.create(user);
        Driver driver = new Driver(0, "водитель 1", user);
        driverRepository.add(driver);

        User user2 = new User(0, "login2", "password2");
        userRepository.create(user2);
        Driver driver2 = new Driver(0, "водитель 1", user2);
        driverRepository.add(driver2);

        List<Driver> expect = List.of(driver, driver2);
        List<Driver> rsl = driverRepository.findAll();
        assertThat(rsl).isEqualTo(expect);
    }

    @Test
    public void whenFindById() {
        User user = new User(0, "login", "password");
        userRepository.create(user);
        Driver driver = new Driver(0, "водитель 1", user);
        driverRepository.add(driver);
        Optional<Driver> rsl = driverRepository.findById(driver.getId());
        assertThat(rsl.get().getName()).isEqualTo("водитель 1");
    }
}