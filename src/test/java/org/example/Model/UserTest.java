package org.example.Model;

import org.example.Model.DAO.UserDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class UserTest {

    private SessionFactory sessionFactory;
    @BeforeEach
    protected void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
        }
    }

    @AfterEach
    protected void tearDown() {
        if(sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void storeUsersInMySQL() {
        User user = new User( "Victor", "1234", false);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Test
    public void verifyUser_DAO() {
        UserDAO userDAO = new UserDAO();
        Assertions.assertTrue(userDAO.verifyUser("Victor", "1234"));
    }
}
