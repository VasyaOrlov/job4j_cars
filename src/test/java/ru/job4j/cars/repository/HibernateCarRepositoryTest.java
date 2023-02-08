package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HibernateCarRepositoryTest {

    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();

    private final CRUDRepository crudRepository = new HibernateCRUDRepository(sf);
    private final CarRepository carRepository = new HibernateCarRepository(crudRepository);
    private final EngineRepository engineRepository = new HibernateEngineRepository(crudRepository);

    @AfterEach
    public void clear() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete Car").executeUpdate();
        session.createQuery("delete Engine").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenAddCar() {
        Engine engine = new Engine(0, "engine");
        Car car = new Car(0, "car", engine, null);
        Optional<Car> rsl = carRepository.add(car);
        assertThat(rsl.get().getName()).isEqualTo("car");

        Car car2 = new Car(car.getId(), "car", engine, null);
        Optional<Car> rsl2 = carRepository.add(car2);
        assertThat(rsl2.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteCar() {
        Engine engine = new Engine(0, "engine");
        Car car = new Car(0, "car", engine, null);
        carRepository.add(car);
        boolean rsl = carRepository.delete(car.getId());
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenUpdateCar() {
        Engine engine = new Engine(0, "engine");
        Car car = new Car(0, "car", engine, null);
        carRepository.add(car);
        Car car2 = new Car(car.getId(), "car2", engine, null);
        boolean rsl = carRepository.update(car2);
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenFindAll() {
        Engine engine = new Engine(0, "engine");
        Car car = new Car(0, "car", engine, null);
        carRepository.add(car);
        Engine engine2 = new Engine(0, "engine");
        Car car2 = new Car(0, "car2", engine2, null);
        carRepository.add(car2);

        List<Car> expect = List.of(car, car2);
        List<Car> rsl = carRepository.findAll();
        assertThat(rsl).isEqualTo(expect);
    }

    @Test
    public void whenFindById() {
        Engine engine = new Engine(0, "engine");
        Car car = new Car(0, "car", engine, null);
        carRepository.add(car);
        Optional<Car> rsl = carRepository.findById(car.getId());
        assertThat(rsl.get().getName()).isEqualTo("car");
    }
}