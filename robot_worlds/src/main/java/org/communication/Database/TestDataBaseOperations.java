package org.communication.Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class TestDataBaseOperations {

    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration().configure().addAnnotatedClass(World.class).buildSessionFactory();

        // Create a World instance
        World world = new World("TestWorld", 0, 0, 100, 100);

        // Save the World instance
        saveWorld(world);

        // Retrieve the World instance
        World retrievedWorld = findWorldByName("TestWorld");
        System.out.println("Retrieved World: " + retrievedWorld);

        // Delete the World instance
        deleteWorldByName("TestWorld");

        // Close the sessionFactory
        sessionFactory.close();
    }

    public static void saveWorld(World world) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(world);
            transaction.commit();
            System.out.println("World saved successfully!");
        }
    }


    public static World findWorldByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM World WHERE name = :name", World.class)
                    .setParameter("name", name)
                    .uniqueResult();
        }
    }

    public static void deleteWorldByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            World world = session.createQuery("FROM World WHERE name = :name", World.class)
                    .setParameter("name", name)
                    .uniqueResult();
            if (world != null) {
                session.delete(world);
                System.out.println("World deleted successfully!");
            }
            transaction.commit();
        }
    }
}
