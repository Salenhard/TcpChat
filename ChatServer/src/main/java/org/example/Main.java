package org.example;

import org.example.controller.ChatController;
import org.example.controller.MessageController;
import org.example.controller.UserController;
import org.example.repository.ChatRepository;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;
import org.example.repository.impl.ChatRepositoryImpl;
import org.example.repository.impl.MessageRepositoryImpl;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.server.ChatServer;
import org.example.service.ChatService;
import org.example.service.MessageService;
import org.example.service.UserService;
import org.example.service.impl.ChatServiceImpl;
import org.example.service.impl.JWTService;
import org.example.service.impl.MessageServiceImpl;
import org.example.service.impl.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        JWTService jwtService = new JWTService();
        UserRepository userRepository = new UserRepositoryImpl();
        ChatRepository chatRepository = new ChatRepositoryImpl();
        MessageRepository messageRepository = new MessageRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepository);
        ChatService chatService = new ChatServiceImpl(chatRepository, userService);
        MessageService messageService = new MessageServiceImpl(messageRepository, chatService, userService);
        ChatController chatController = new ChatController(chatService);
        UserController userController = new UserController(userService, jwtService);
        MessageController messageController = new MessageController(messageService);
        ChatServer chatServer = new ChatServer(chatController, messageController, userController);
        chatServer.run();
    }
}
