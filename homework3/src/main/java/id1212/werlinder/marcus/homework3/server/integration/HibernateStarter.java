package id1212.werlinder.marcus.homework3.server.integration;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public abstract class HibernateStarter {

    private static SessionFactory sessionFactory;

    /**
     * Initialize a session factory
     */
    public static void initSessionFactory() {
        if (sessionFactory == null)
            sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
