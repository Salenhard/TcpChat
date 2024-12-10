package org.example.service;

import org.example.entity.Chat;
import org.example.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChatService {

    List<Chat> findAll(String name, List<User> users);

    Optional<Chat> findById(Long id);

    Chat save(Chat chat, String username);

    Chat update(Long id, Chat chat, String username);

    void deleteById(Long id, String username);

    void addUser(long id, String username, String caller);

    void deleteAll();

    void removeUser(Long id, String username, String caller);
}
