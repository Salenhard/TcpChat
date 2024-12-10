package org.example.repository;

import org.example.entity.Chat;
import org.example.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    List<Chat> findAll(String name, Boolean isPrivate, List<User> users);

    Chat save(Chat chat);

    Optional<Chat> findById(Long id);

    void deleteById(Long id);

    void deleteAll();
}
