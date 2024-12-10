package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Chat;
import org.example.entity.Message;
import org.example.entity.User;
import org.example.repository.MessageRepository;
import org.example.service.ChatService;
import org.example.service.MessageService;
import org.example.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository repository;

    private final ChatService chatService;

    private final UserService userService;

    @Override
    public List<Message> findAll(String username, String text, LocalDateTime from, LocalDateTime till, Long chatId, String usernameCaller) {
        List<Message> messages = repository.findAll(username, text, from, till, chatId);
        messages.stream().filter(message -> !message.getAuthor().getUsername().equals(usernameCaller)).forEach(message -> {
            message.setIsRead(true);
            repository.save(message);
        });
        return messages;
    }

    @Override
    public Optional<Message> findByIdAndChatId(Long id, Long chatId) {
        return repository.findByIdAndChatId(id, chatId);
    }

    @Override
    public Message save(Message message, Long chatId, String username) {
        Chat chat = chatService.findById(chatId).orElseThrow(() -> new RuntimeException("Chat with id %d not found".formatted(chatId)));
        User user = userService.findById(username).orElseThrow(() -> new RuntimeException("User with id %d not found".formatted(username)));
        if (!chat.getUsers().contains(user))
            throw new RuntimeException("User with id %s not in chat with id %d".formatted(username, chatId));
        message.setCreatedAt(LocalDateTime.now());
        message.setIsEdited(false);
        message.setIsRead(false);
        message.setAuthor(user);
        message.setChat(chat);
        return repository.save(message);
    }

    @Override
    public Message update(Long id, Message message, Long chatId, String username) {
        User user = userService.findById(username).orElseThrow(() -> new RuntimeException("User with id %s not found".formatted(username)));
        Message newMessage = repository.findByIdAndChatId(id, chatId).orElseThrow(() -> new RuntimeException("Message with id %d not found".formatted(id)));
        if (!newMessage.getAuthor().equals(user))
            throw new RuntimeException("Not allowed to edit this message with id: %d".formatted(id));
        newMessage.setText(message.getText());
        newMessage.setEditedAt(LocalDateTime.now());
        newMessage.setIsEdited(true);
        return repository.save(newMessage);
    }

    @Override
    public void deleteByIdAndChatId(Long id, Long chatId, String username) {
        User user = userService.findById(username).orElseThrow(() -> new RuntimeException("User with id %d not found".formatted(username)));
        Message message = repository.findByIdAndChatId(id, chatId).orElseThrow(() -> new RuntimeException("Message with id %d not found".formatted(id)));
        if (!message.getAuthor().equals(user))
            throw new RuntimeException("Not allowed to delete this message with id: %d".formatted(id));
        repository.deleteByIdAndChatId(id, chatId);
    }
}
