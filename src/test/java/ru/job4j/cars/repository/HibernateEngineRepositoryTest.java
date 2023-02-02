package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HibernateEngineRepositoryTest {

    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();

    private final CRUDRepository crudRepository = new HibernateCRUDRepository(sf);
    private final EngineRepository engineRepository =
            new HibernateEngineRepository(crudRepository);

    @AfterEach
    public void clear() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete Engine").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenAddEngine() {
        Engine engine = new Engine(0, "двигатель 1");
        Optional<Engine> rsl = engineRepository.add(engine);
        String rslName = rsl.isEmpty() ? null : rsl.get().getName();
        assertThat(rslName).isEqualTo("двигатель 1");

        Engine engine1 = new Engine(engine.getId(), "двигатель 2");
        Optional<Engine> rsl1 = engineRepository.add(engine1);
        assertThat(rsl1.isEmpty()).isTrue();
    }

    @Test
    public void whenDeleteEngine() {
        Engine engine = new Engine(0, "двигатель 1");
        engineRepository.add(engine);
        int id = engine.getId();
        boolean rsl = engineRepository.delete(id);
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenUpdateEngine() {
        Engine engine = new Engine(0, "двигатель 1");
        engineRepository.add(engine);
        Engine engine1 = new Engine(engine.getId(), "engine 1");
        boolean rsl = engineRepository.update(engine1);
        assertThat(rsl).isTrue();
    }

    @Test
    public void whenFindAll() {
        Engine engine1 = new Engine(0, "двигатель 1");
        engineRepository.add(engine1);
        Engine engine2 = new Engine(0, "двигатель 2");
        engineRepository.add(engine2);
        Engine engine3 = new Engine(0, "двигатель 3");
        engineRepository.add(engine3);
        List<Engine> expect = List.of(engine1, engine2, engine3);
        List<Engine> rsl = engineRepository.findAll();
        assertThat(rsl).isEqualTo(expect);
    }

    @Test
    public void whenFindById() {
        Engine engine = new Engine(0, "двигатель 1");
        engineRepository.add(engine);
        Optional<Engine> rsl = engineRepository.findById(engine.getId());
        assertThat(rsl.get().getName()).isEqualTo("двигатель 1");
    }
}