package org.example.service;

import org.example.entity.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageService {

    List<Message> findAll(String username, String text, LocalDateTime from, LocalDateTime till, Long chatId, String usernameCaller);

    Optional<Message> findByIdAndChatId(Long id, Long chatId);

    Message save(Message message, Long chatId, String username);

    Message update(Long id, Message message, Long chatId, String username);

    void deleteByIdAndChatId(Long id, Long chatId, String username);

}
