package salen.loginService.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import salen.loginService.entity.User;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);

}
