package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * класс HibernateCRUDRepository реализует интерфейс CRUDRepository
 * класс реализует инкапсуляцию запросов хранилищ объектов
 */
@Repository
@ThreadSafe
@AllArgsConstructor
public class HibernateCRUDRepository implements CRUDRepository {

    private SessionFactory sf;

    @Override
    public <T> Optional<T> optional(Consumer<Session> con) {
        return tx(e -> {
            con.accept(e);
            return Optional.empty();
        });
    }

    @Override
    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> map) {
        Function<Session, Optional<T>> com = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : map.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.uniqueResultOptional();
        };
        return tx(com);
    }

    @Override
    public boolean total(String query, Map<String, Object> map) {
        Function<Session, Boolean> com = session -> {
            Query sq = session
                    .createQuery(query);
            for (Map.Entry<String, Object> arg : map.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.executeUpdate() != 0;
        };
        return tx(com);
    }

    @Override
    public boolean total(Function<Session, Boolean> com) {
        return tx(com);
    }

    @Override
    public <T> List<T> list(String query, Class<T> cl) {
        Function<Session, List<T>> com = session -> {
            Query<T> sq = session.createQuery(query, cl);
            return sq.list();
        };
        return tx(com);
    }

    @Override
    public <T> List<T> list(String query, Class<T> cl, Map<String, Object> map) {
        Function<Session, List<T>> com = session -> {
            Query<T> sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : map.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.list();
        };
        return tx(com);
    }

    public <T> T tx(Function<Session, T> command) {
        Session session = sf.openSession();
        try (session) {
            Transaction tx = session.beginTransaction();
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (Exception e) {
            Transaction tx = session.getTransaction();
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}
