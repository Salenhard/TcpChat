package org.example.repository.impl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.repository.ChatRepository;
import org.example.repository.Repository;
import java.util.List;
import java.util.Optional;

public class ChatRepositoryImpl extends Repository<Chat, Long> implements ChatRepository {

    @Override
    public List<Chat> findAll(String name, Boolean isPrivate, List<User> users) {

        StringBuilder sql = new StringBuilder("SELECT c FROM Chat c JOIN FETCH c.users JOIN FETCH c.creator");
        boolean first = true;
        if (name != null || isPrivate != null || users != null)
            sql.append(" WHERE");
        if (name != null) {
            sql.append(" c.name LIKE ").append("'%").append(name).append("%'");
            first = false;
        }
        if (isPrivate != null)
            if (!first)
                sql.append(" AND c.isPrivate = ").append(isPrivate);
            else {
                sql.append(" c.isPrivate = ").append(isPrivate);
                first = false;
            }
        return emf.createEntityManager().createQuery(sql.toString(), Chat.class).getResultList();
    }

    @Override
    public Chat save(Chat chat) {
        EntityTransaction tx = null;
        try (EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            if (chat.getId() != null) {
                chat = em.merge(chat);
            } else {
                em.persist(chat);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return chat;
    }

    @Override
    public Optional<Chat> findById(Long key) {
        return Optional.ofNullable(emf.createEntityManager().find(Chat.class, key));
    }

    @Override
    public void delete(Chat chat) {
        EntityTransaction tx = null;
        try (EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.remove(em.contains(chat) ? chat : em.merge(chat));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    public void deleteAll() {
        EntityTransaction tx = null;
        try (EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.createNamedQuery("Chat.deleteAll").executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
