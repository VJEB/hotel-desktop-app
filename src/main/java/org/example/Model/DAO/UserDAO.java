package org.example.Model.DAO;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.Model.User_;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.example.Model.User;
import org.example.Model.HibernateUtil;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;

public class UserDAO {
    public void storeUser(User user) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
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
    public boolean verifyUser(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(User_.USERNAME), username.toLowerCase()));
            TypedQuery<User> query = session.createQuery(criteriaQuery);
            User user = query.getSingleResult();
            session.close();
            System.out.println(user.getUser_password() + " " + User.encryptUserPassword(password));
            return Objects.equals(user.getUser_password(), User.encryptUserPassword(password));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("There is no user with that username", e);
        }
    }
}

