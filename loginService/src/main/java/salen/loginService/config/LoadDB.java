package salen.loginService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import salen.loginService.entity.Role;
import salen.loginService.entity.User;
import salen.loginService.service.UserService;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class LoadDB {

    private final UserService service;

    @Bean
    public CommandLineRunner initDB() {
        return args -> {
            service.save(new User("test", "test", Set.of(Role.ADMIN, Role.USER), true));
        };

    }
}
