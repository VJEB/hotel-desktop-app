package org.example.Model.DAO;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.collections.FXCollections;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.example.Model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class GuestDAO {
    private static List<Guest> cachedGuests;

    public static List<Guest> getCachedGuests() {
        return cachedGuests;
    }
    public static void setCachedGuests(List<Guest> guests) {
        cachedGuests = guests;
    }
    public static void addGuestToCachedGuests(Guest guest) {
       cachedGuests.add(guest);
    }
    public void storeGuest(Guest guest) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(guest);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }
    public boolean deleteGuest(String guestNationalId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Guest> criteriaQuery = criteriaBuilder.createQuery(Guest.class);
            Root<Guest> root = criteriaQuery.from(Guest.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Guest_.NATIONAL_ID), guestNationalId));

            TypedQuery<Guest> query = session.createQuery(criteriaQuery);
            Guest guest = query.getSingleResult();

            if (guest != null) {
                // Delete the guest
                session.beginTransaction();
                session.delete(guest);
                session.getTransaction().commit();

                return true;
            } else {
                return false; // Guest not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting guest with national id of: " + guestNationalId, e);
        }
    }
    public List<Guest> selectGuestsFromDB() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Guest> criteriaQuery = criteriaBuilder.createQuery(Guest.class);
            Root<Guest> root = criteriaQuery.from(Guest.class);
            criteriaQuery.select(root);
            TypedQuery<Guest> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting guests from DB", e);
        }
    }
}
