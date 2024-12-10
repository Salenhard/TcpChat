package salen.loginService.repository.datajpa;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import salen.loginService.entity.Role;
import salen.loginService.entity.User;
import salen.loginService.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static salen.loginService.util.UserSpecification.*;

@Repository
@RequiredArgsConstructor
public class UserDataJpaRepository implements UserRepository {

    public final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Page<User> findAll(List<Role> roles, String username, Pageable pageable) {
        Specification<User> specification = Specification.where(Objects.nonNull(username) ? likeUsername(username) : null)
                .and(isEnabled(true))
                .and(Objects.nonNull(roles) ? rolesIn(roles) : null);
        return userJpaRepository.findAll(specification, pageable);
    }

    @Override
    public void deleteByUsername(String username) {
        userJpaRepository.deleteByUsername(username);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
