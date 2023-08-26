package org.example.Model;

import org.example.Model.DAO.GuestDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

public class GuestTest {
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
    public void storeGuestInMySQL() {
        Guest guest = new Guest("0501-2005-04202", "Victor Jafet", "Espinoza Benitez", LocalDate.of(2005, 3, 3), "31466774", "vespinozabenitez@gmail.com", GuestNationalities.HONDURAS);

        //for (int i = 0; i < 50; i++) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    //session.persist(new Guest("0501-2005-042" + String.valueOf(i), "Victor Jafet", "Espinoza Benitez", LocalDate.of(2005, 3, 3), "31466774", "vespinozabenitez@gmail.com", GuestNationalities.HONDURAS));
                    session.persist(guest);
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                    e.printStackTrace();
                }
            }
        //}
    }
    @Test
    public void deleteGuestFromMySQL(){
        GuestDAO guestDAO = new GuestDAO();
        Assertions.assertTrue(guestDAO.deleteGuest("0501-2005-04202"));
    }

    @Test
    public void getGuestsListFromDB(){
        GuestDAO guestDAO = new GuestDAO();
        List<Guest> resultGuests = guestDAO.selectGuestsFromDB();

        for (Guest guest : resultGuests) {
            System.out.println("National ID: " + guest.getNational_id());
            System.out.println("First Name: " + guest.getFirstName());
            System.out.println("Last Name: " + guest.getLastName());
            System.out.println("Date of birth: " + guest.getDateOfBirth());
            System.out.println("Phone number: " + guest.getPhoneNumber());
            System.out.println("Email: " + guest.getEmail());
            System.out.println("Nationality: " + guest.getNationality());
            System.out.println("=============================");
        }
    }
    @Test
    public void updateGuestInMySQL() {
        Guest guest = new Guest("1234", "pepito", "juarez", LocalDate.of(2005, 3, 3), "12345678", "pepitojuarezEDITADO@gmail.com", GuestNationalities.UNITED_STATES);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(guest);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }
}
