package org.example.repository;

import org.example.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    void deleteById(String username);

    void deleteAll();
}
