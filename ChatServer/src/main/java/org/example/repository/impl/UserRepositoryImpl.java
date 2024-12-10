package org.example.repository.impl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.entity.User;
import org.example.repository.Repository;
import org.example.repository.UserRepository;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl extends Repository<User, String> implements UserRepository {
    @Override
    protected Optional<User> findById(String key) {
        return Optional.ofNullable(emf.createEntityManager().find(User.class, key));
    }

    @Override
    public User save(User user) {
        EntityTransaction tx = null;
        try (EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findById(username);
    }

    @Override
    public List<User> findAll() {
        return emf.createEntityManager().createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public void deleteById(String username) {
        findById(username).ifPresent(this::delete);
    }

    @Override
    public void deleteAll() {
        EntityTransaction tx = null;
        try (EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(User user) {
        EntityTransaction tx = null;
        try (EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.remove(em.contains(user) ? user : em.merge(user));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
