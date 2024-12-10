package salen.loginService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import salen.loginService.entity.Role;
import salen.loginService.entity.User;
import salen.loginService.repository.UserRepository;
import salen.loginService.service.UserService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authManger;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    private final JWTService jwtService;


    @Override
    public Optional<User> findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public User save(User user) {
        if (findByUsername(user.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with username `%s` already exists".formatted(user.getUsername()));
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteByUsername(String userName) {
        Optional<User> byUsername = findByUsername(userName);
        if (byUsername.isEmpty())
            return;
        User user = byUsername.get();
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public Page<User> findAll(List<Role> roles, String userName, Pageable pageable) {
        return userRepository.findAll(roles, userName, pageable);
    }

    @Override
    public User update(String username, User newUser) {
        User updatedUser = userRepository.findByUsername(username).map(user ->
        {
            user.setPassword(encoder.encode(newUser.getPassword()));
            user.setRoles(newUser.getRoles());
            user.setEnabled(newUser.isEnabled());
            return user;
        }).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username `%s` not found".formatted(username)));
        return userRepository.save(updatedUser);
    }

    @Override
    public String verify(User user) {
        Authentication authentication =
                authManger.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (!authentication.isAuthenticated())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        return jwtService.generateToken(user.getUsername());
    }
}
