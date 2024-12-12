package org.example.server;

import lombok.RequiredArgsConstructor;
import org.example.controller.ChatController;
import org.example.controller.MessageController;
import org.example.controller.UserController;
import org.example.dto.ChatDto;
import org.example.dto.MessageDto;
import org.example.dto.UserDto;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class ChatServer {
    private static final int PORT = 12345;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final ChatController chatController;
    private final MessageController messageController;
    private final UserController userController;

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and waiting for connections..");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket, chatController, messageController, userController);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isOnline(String username) {
        return clients.stream().anyMatch(client -> client.username.equals(username));
    }

    private static void broadcast(MessageDto message, ClientHandler sender) {
       clients.stream().filter(client ->
                       client != sender
                       && sender.getChatId().equals(client.chatId))
               .forEach(client -> client.sendMessage(message));
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final ChatController chatController;
        private final MessageController messageController;
        private final UserController userController;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        private Long chatId;

        public ClientHandler(Socket socket, ChatController chatController, MessageController messageController, UserController userController) {
            this.clientSocket = socket;
            this.chatController = chatController;
            this.messageController = messageController;
            this.userController = userController;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                if (!validate()) {
                    out.println("Jwt token is invalid");
                    close();
                    return;
                }
                if (isOnline(username)) {
                    out.printf("User with this username:'$s' is are already online%n", username);
                    close();
                    return;
                }
                clients.add(this);
                try {
                    userController.get(username);
                } catch (RuntimeException e) {
                    userController.add(new UserDto(username));
                }
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    try {
                        handleRequests(inputLine);
                    } catch (RuntimeException e) {
                        out.println(e.getMessage());
                        System.out.println(e.getMessage());
                    }
                }
                clients.remove(this);
                close();
            } catch (IOException e) {
                System.out.printf("Client '%s' disconnected%n", username);
            } finally {
                clients.remove(this);
            }
        }

        public void close() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
        }


        private void handleRequests(String inputLine) {
            if (inputLine.startsWith("createChat")) {
                ChatDto chat = parseChat(inputLine.substring("createChat".length() + 1));
                chat = chatController.save(chat, username);
                out.println("Chat created: " + chat);
            } else if (inputLine.startsWith("editChat")) {
                ChatDto chat = parseChat(inputLine.substring("createChat".length() + 1));
                chat = chatController.update(chat.getId(), chat, username);
                out.println("Chat updated: " + chat);
            } else if (inputLine.startsWith("deleteChat")) {
                chatController.delete(Long.valueOf(inputLine.split(" ")[1]), username);
                out.println("Chat deleted");
            } else if (inputLine.startsWith("join")) {
                chatId = Long.valueOf(inputLine.split(" ")[1]);
                ChatDto chat = chatController.get(chatId);
                if (!chat.getUsers().contains(userController.get(username)))
                    chatController.addUser(chatId, username, username);
                out.println("You have joined the chat with id " + chatId);
                loadHistory(chatId);
            } else if (inputLine.startsWith("message")) {
                MessageDto dto = parseMessage(inputLine);
                MessageDto message;
                if (dto.getId() == null)
                    message = messageController.addMessage(dto, dto.getChatId(), username);
                else
                    message = messageController.updateMessage(dto.getId(), dto, dto.getChatId(), username);
                sendMessage(message);
                broadcast(message, this);
            } else if (inputLine.startsWith("delete")) {
                messageController.deleteMessage(Long.valueOf(inputLine.split(" ")[1]), Long.valueOf(inputLine.split(" ")[2]), username);
            } else if (inputLine.startsWith("add")) {
                ChatDto chat = chatController.get(Long.valueOf(inputLine.split(" ")[1]));
                chat.getUsers().add(userController.get(inputLine.split(" ")[2]));
                chatController.update(chat.getId(), chat, username);
                out.println("User added to chat");
            } else if (inputLine.startsWith("remove")) {
                ChatDto chat = chatController.get(Long.valueOf(inputLine.split(" ")[1]));
                chat.getUsers().remove(userController.get(inputLine.split(" ")[2]));
                chatController.update(chat.getId(), chat, username);
                out.println("User removed from chat");
            } else if (inputLine.equals("list")) {
                List<ChatDto> chats = chatController.getAll("", null);
                out.println("List of chats:");
                chats.forEach(chat -> out.println(chat.getId() + " " + chat.getName() + (chat.getUsers().stream().anyMatch(user -> user.getUsername().equals(username)) ? "(you are member of this chat)" : "")));
            }
        }

        private boolean validate() throws IOException {
            String token = in.readLine();
            try {
                username = userController.validate(token);
            } catch (RuntimeException e) {
                return false;
            }
            return true;
        }

        private void loadHistory(Long chatId) {
            List<MessageDto> messages = messageController.getMessages(null, null, null, null, chatId, username);
            messages.forEach(this::sendMessage);
        }

        private MessageDto parseMessage(String message) {
            MessageDto msg = new MessageDto();
            JSONObject json = new JSONObject(message.substring(7));
            try {
                msg.setId(json.getLong("id"));
            } catch (RuntimeException e) {
                msg.setId(null);
            }
            msg.setText(json.getString("text"));
            msg.setChatId(json.getLong("chatId"));
            return msg;
        }

        private ChatDto parseChat(String chat) {
            ChatDto chatEntity = new ChatDto();
            JSONObject json = new JSONObject(chat);
            try {
                chatEntity.setId(json.getLong("id"));
            } catch (RuntimeException e) {
                chatEntity.setId(null);
            }
            chatEntity.setName(json.getString("name"));
            chatEntity.setIsPrivate(json.getBoolean("isPrivate"));
            return chatEntity;
        }

        private String getUsername() throws IOException {
            return in.readLine();
        }

        public void sendMessage(MessageDto message) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
            out.println("[" + formatter.format(message.getCreatedAt()) + "]" + ":[" + message.getAuthor().getUsername() + "]:" + message.getText() + " :id:" + message.getId() + (message.getIsRead() ? " read" : ""));
        }

        public Long getChatId() {
            return chatId;
        }
    }
}