package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.dto.mapper.UserMapper;
import org.example.service.UserService;
import org.example.service.impl.JWTService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper mapper = UserMapper.INSTANCE;
    private final JWTService jwtService;

    public UserDto get(String id) {
        return service.findById(id).map(mapper::toDto).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String validate(String token) throws RuntimeException {
        String username = "";
        username = jwtService.validateToken(token);
        return username;
    }

    public UserDto add(@Valid UserDto dto) {
        return mapper.toDto(service.save(mapper.toEntity(dto)));
    }

    public void delete(String id) {
        service.deleteById(id);
    }

    public List<UserDto> getAll() {
        return service.findAll().stream().map(mapper::toDto).toList();
    }

    public void deleteAll() {
        service.deleteAll();
    }
}
