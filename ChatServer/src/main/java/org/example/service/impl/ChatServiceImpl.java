package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.repository.ChatRepository;
import org.example.service.ChatService;
import org.example.service.UserService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository repository;
    private final UserService userService;

    @Override
    public void addUser(long id, String username, String caller) {
        Chat chat = repository.findById(id).orElseThrow(() -> new RuntimeException("Chat with id:%d not found".formatted(id)));
        User user = userService.findById(username).orElseThrow(() -> new RuntimeException("User with username:%s not found".formatted(username)));
        User callerUser = userService.findById(caller).orElseThrow(() -> new RuntimeException("User with username:%s not found".formatted(caller)));
        if (chat.getIsPrivate() && !chat.getCreator().equals(callerUser))
            throw new RuntimeException("You are not allowed to add users to this private chat");
        chat.getUsers().add(user);
        repository.save(chat);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void removeUser(Long id, String username, String caller) {
        Chat chat = repository.findById(id).orElseThrow(() -> new RuntimeException("Chat with id:%d not found".formatted(id)));
        User user = userService.findById(username).orElseThrow(() -> new RuntimeException("User with username:%s not found".formatted(username)));
        User callerUser = userService.findById(caller).orElseThrow(() -> new RuntimeException("User with username:%s not found".formatted(caller)));
        if (user.equals(callerUser) && user.equals(chat.getCreator()))
            throw new RuntimeException("You are not allowed to remove yourself as creator");
        if (!callerUser.equals(chat.getCreator()) && !user.equals(callerUser))
            throw new RuntimeException("You are not allowed to remove users from chat");
        chat.getUsers().remove(user);
        repository.save(chat);
    }

    @Override
    public List<Chat> findAll(String name, List<User> users) {
        return repository.findAll(name, false, users);
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Chat save(Chat chat, String username) {
        User user = userService.findById(username).orElseThrow(() -> new RuntimeException("User with username:%s not found".formatted(username)));
        chat.setCreator(user);
        chat.setUsers(List.of(user));
        return repository.save(chat);
    }

    @Override
    public Chat update(Long id, Chat chat, String username) {
        User user = userService.findById(username).orElseThrow(() -> new RuntimeException("User with username:%s not found".formatted(username)));
        if (!chat.getCreator().equals(user))
            throw new RuntimeException("You are not allowed to update this chat");
        Chat newChat = repository.findById(id)
                .map(updatedChat -> {
                    updatedChat.setName(chat.getName());
                    updatedChat.setIsPrivate(chat.getIsPrivate());
                    updatedChat.setUsers(chat.getUsers());
                    return updatedChat;
                })
                .orElseThrow(() -> new RuntimeException("Chat with id:%d not found".formatted(id)));
        return repository.save(newChat);
    }

    @Override
    public void deleteById(Long id, String username) {
        User user = userService.findById(username).orElseThrow(() -> new RuntimeException("User with username:%s not found".formatted(username)));
        Chat chat = repository.findById(id).orElseThrow(() -> new RuntimeException("Chat with id:%d not found".formatted(id)));
        if (!chat.getCreator().equals(user))
            throw new RuntimeException("You are not allowed to delete this chat");
        repository.deleteById(id);
    }


}
