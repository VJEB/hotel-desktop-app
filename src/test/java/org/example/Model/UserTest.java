package org.example.Model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {

    private SessionFactory sessionFactory;
    @BeforeEach
    protected void setUp() throws Exception {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @AfterEach
    protected void tearDown() throws Exception {
        if(sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void storeUsersInMySQL() {

        byte[] byteArray = { 72, 101, 108, 108, 111 }; // Represents the ASCII values of the characters "Hello"
        User user = new User(byteArray, "Lisa", "1234", false);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(user);

            session.getTransaction().commit();
        }
    }
}
