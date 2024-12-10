package salen.loginService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import salen.loginService.entity.Role;
import salen.loginService.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    Page<User> findAll(List<Role> roles, String username, Pageable pageable);

    void deleteByUsername(String username);

    User save(User user);

}