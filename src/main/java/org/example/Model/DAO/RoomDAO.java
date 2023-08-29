package org.example.Model.DAO;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.Model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class RoomDAO {
    private static List<Room> cachedRooms;

    public static List<Room> getCachedRooms() {
        return cachedRooms;
    }
    public static void setCachedRooms(List<Room> rooms) {
        cachedRooms = rooms;
    }
    public static void addRoomToCachedRooms(Room room) {
        cachedRooms.add(room);
    }
    public static void removeRoomFromCachedRooms(Room room) {
        cachedRooms.remove(room);
    }
    public static void updateRoomFromCachedRooms(Room roomToBeUpdated, Room room){
        cachedRooms.remove(roomToBeUpdated);
        cachedRooms.add(room);
    }
    public void storeRoom(Room room) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(room);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }
    public void updateRoom(Room room) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(room);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    public boolean deleteRoom(String roomNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Room> criteriaQuery = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = criteriaQuery.from(Room.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Room_.ROOM_NUMBER), roomNumber));

            TypedQuery<Room> query = session.createQuery(criteriaQuery);
            Room room = query.getSingleResult();

            if (room != null) {
                session.beginTransaction();
                session.delete(room);
                session.getTransaction().commit();

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting room with room-number of: " + roomNumber, e);
        }
    }

    public List<Room> selectRoomsFromDB() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Room> criteriaQuery = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = criteriaQuery.from(Room.class);
            criteriaQuery.select(root);
            TypedQuery<Room> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting rooms from DB", e);
        }
    }
}
