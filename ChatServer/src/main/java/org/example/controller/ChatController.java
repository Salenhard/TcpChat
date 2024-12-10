package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChatDto;
import org.example.dto.UserDto;
import org.example.dto.mapper.ChatMapper;
import org.example.dto.mapper.UserMapper;
import org.example.entity.User;
import org.example.service.ChatService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;
    private final ChatMapper mapper = ChatMapper.INSTANCE;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public void delete(Long id, String username) {
        service.deleteById(id, username);
    }

    public ChatDto update(Long id, @Valid ChatDto dto, String username) {
        return mapper.toDto(service.update(id, mapper.toEntity(dto), username));
    }

    public void addUser(long id, String username, String caller) {
        service.addUser(id, username, caller);
    }

    public ChatDto save(@Valid ChatDto dto, String username) {
        return mapper.toDto(service.save(mapper.toEntity(dto), username));
    }

    public ChatDto get(Long id) {
        return service.findById(id).map(mapper::toDto).orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    public List<ChatDto> getAll(String name, List<UserDto> users) {
        List<User> list = null;
        if(users != null)
            list = users.stream().map(userMapper::toEntity).toList();
        return service.findAll(name, list).stream().map(mapper::toDto).toList();
    }

    public void deleteAll() {
        service.deleteAll();
    }

    public void removeUser(Long id, String username, String caller) {
        service.removeUser(id, username, caller);
    }

}
