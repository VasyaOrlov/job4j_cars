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

    @AfterEach
    public void clear() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete Driver").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenAddDriver() {
        Driver driver = new Driver(0, "водитель 1");
        Optional<Driver> rsl = driverRepository.add(driver);
        String rslName = rsl.isEmpty() ? null : rsl.get().getName();
        assertThat(rslName).isEqualTo("водитель 1");

        Driver driver1 = new Driver(driver.getId(), "водитель 2");
        Optional<Driver> rsl1 = driverRepository.add(driver1);
        assertThat(rsl1.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteDriver() {
        Driver driver = new Driver(0, "водитель 1");
        driverRepository.add(driver);
        boolean rsl = driverRepository.delete(driver.getId());
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenUpdateDriver() {
        Driver driver = new Driver(0, "водитель 1");
        driverRepository.add(driver);
        Driver driver2 = new Driver(driver.getId(), "водитель 2");
        boolean rsl = driverRepository.update(driver2);
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenFindAll() {
        Driver driver = new Driver(0, "водитель 1");
        driverRepository.add(driver);

        Driver driver2 = new Driver(0, "водитель 1");
        driverRepository.add(driver2);

        List<Driver> expect = List.of(driver, driver2);
        List<Driver> rsl = driverRepository.findAll();
        assertThat(rsl).isEqualTo(expect);
    }

    @Test
    public void whenFindById() {
        Driver driver = new Driver(0, "водитель 1");
        driverRepository.add(driver);
        Optional<Driver> rsl = driverRepository.findById(driver.getId());
        assertThat(rsl.get().getName()).isEqualTo("водитель 1");
    }
}