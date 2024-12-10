package org.example.repository;

import org.example.entity.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository {

    List<Message> findAll(String username, String text, LocalDateTime from, LocalDateTime till, Long chatId);

    Optional<Message> findByIdAndChatId(Long id, Long chatId);

    Message save(Message message);

    void deleteById(Long id);

    void deleteByIdAndChatId(Long id, Long chatId);

}
