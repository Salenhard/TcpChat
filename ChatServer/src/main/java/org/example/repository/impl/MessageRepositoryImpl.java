package org.example.repository.impl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.entity.Message;
import org.example.repository.MessageRepository;
import org.example.repository.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MessageRepositoryImpl extends Repository<Message, Long> implements MessageRepository {
    @Override
    public List<Message> findAll(String username, String text, LocalDateTime from, LocalDateTime till, Long chatId) {
        return emf.createEntityManager()
                .createNamedQuery("findAllByChatId", Message.class)
                .setParameter("chatId", chatId)
                .getResultList();
    }

    @Override
    public Optional<Message> findByIdAndChatId(Long id, Long chatId) {
        return Optional.ofNullable(emf.createEntityManager()
                .createNamedQuery("findByIdAndChatId", Message.class)
                .setParameter("id", id)
                .setParameter("chatId", chatId)
                .getSingleResult());
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    public Message save(Message message) {
        EntityTransaction tx = null;
        try (EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            if (message.getId() != null) {
                message = em.merge(message);
            } else {
                em.persist(message);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return message;
    }

    @Override
    protected Optional<Message> findById(Long key) {
        return Optional.ofNullable(emf.createEntityManager()
                .find(Message.class, key));
    }

    @Override
    protected void delete(Message message) {
        EntityTransaction tx = null;
        try (EntityManager em = emf.createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.remove(em.contains(message) ? message : em.merge(message));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteByIdAndChatId(Long id, Long chatId) {
        findByIdAndChatId(id, chatId).ifPresent(this::delete);
    }
}
