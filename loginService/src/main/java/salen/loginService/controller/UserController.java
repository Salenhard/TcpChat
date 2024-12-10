package salen.loginService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import salen.loginService.entity.Role;
import salen.loginService.entity.User;
import salen.loginService.entity.dto.UserDto;
import salen.loginService.entity.dto.UserMapper;
import salen.loginService.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public PagedModel<UserDto> getList(@RequestParam(name = "username", required = false) String username,
                                       @RequestParam(name = "roles", required = false) List<Role> roles,
                                       @RequestParam(name = "page", required = false) Optional<Integer> page,
                                       @RequestParam(name = "size", required = false) Optional<Integer> size) {
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(10));
        Page<UserDto> userDtos = userService.findAll(roles, username, pageable).map(mapper::toDto);
        return new PagedModel<>(userDtos);
    }

    @GetMapping("/{username}")
    public UserDto get(@PathVariable("username") String username) {
        Optional<User> user = userService.findByUsername(username);
        return mapper.toDto(user.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username `%s` not found".formatted(username))));
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto dto) {
        return mapper.toDto(userService.save(mapper.toEntity(dto)));
    }

    @PutMapping("/{username}")
    public UserDto update(@PathVariable("username") String username, @RequestBody @Valid UserDto dto) {
        return mapper.toDto(userService.update(username, mapper.toEntity(dto)));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
