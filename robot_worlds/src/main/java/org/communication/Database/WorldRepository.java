package org.communication.Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

public class WorldRepository {

    private SessionFactory sessionFactory;

    public WorldRepository() {
        this.sessionFactory = new Configuration().configure().addAnnotatedClass(World.class).buildSessionFactory();
    }

    public void save(World world) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(world);
            transaction.commit();
        }
    }

    public Optional<World> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            World world = session.createQuery("FROM World WHERE name = :name", World.class)
                    .setParameter("name", name)
                    .uniqueResult();
            return Optional.ofNullable(world);
        }
    }

    public void deleteByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            World world = session.createQuery("FROM World WHERE name = :name", World.class)
                    .setParameter("name", name)
                    .uniqueResult();
            if (world != null) {
                session.delete(world);
            }
            transaction.commit();
        }
    }
}
