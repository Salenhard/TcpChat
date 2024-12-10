package org.example.service;

import org.example.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> findById(String id);

    List<User> findAll();

    void deleteById(String id);

    void deleteAll();
}
