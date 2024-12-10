package salen.loginService.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import salen.loginService.entity.Role;
import salen.loginService.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String userName);

    User save(User user);

    void deleteByUsername(String userName);

    Page<User> findAll(List<Role> roles, String userName, Pageable pageable);

    User update(String username, User newUser);

    String verify(User user);
}
