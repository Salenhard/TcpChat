package org.example.client;

import org.example.entity.dto.Chat;
import org.example.entity.dto.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private final String username;
    private final String jwtToken;
    public Client(String username, String jwtToken) {
        this.username = username;
        this.jwtToken = jwtToken;
    }

    public void run() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the chat server!");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            String userInput;
            out.println(jwtToken);
            out.println(username);
            long chatId = 0L;
            while (true) {
                Long id = null;
                userInput = scanner.nextLine();

                if (userInput.equals("/exit"))
                    break;

                if (userInput.equals("/help")) {
                    System.out.println("Available commands:");
                    System.out.println("/createChat <chatName> <isPrivate> - creates a new chat with the given chatName and isPrivate default is false");
                    System.out.println("/deleteChat <chatId> - deletes the chat with the given chatId");
                    System.out.println("/editChat <chatId> <chatName> <?isPrivate> - edits the chat with the given chatId");
                    System.out.println("/join <chatId> - joins the chat with the given chatId");
                    System.out.println("/list - lists of all chats that user member of");
                    System.out.println("/search <name> - searches for the chats with the given name");
                    System.out.println("/edit <id> <text> - edits the message with the given id");
                    System.out.println("/leave - leaves current chat");
                    System.out.println("/exit - exits chatService");
                    System.out.println("/add <chatId> <user> - adds the user to the chat with the given chatId");
                    System.out.println("/remove <chatId> <user> - removes the user from the chat with the given chatId");
                    continue;
                }

                if (userInput.startsWith("/createChat")) {
                    Chat chat = new Chat();
                    chat.setName(userInput.split(" ")[1]);
                    if (userInput.split(" ").length != 3)
                        chat.setIsPrivate(false);
                    else
                        chat.setIsPrivate(Boolean.parseBoolean(userInput.split(" ")[2]));
                    out.println("createChat " + chat.toJson());
                    continue;
                }

                if (userInput.startsWith("/editChat")) {
                    if (userInput.split(" ").length != 3)
                        out.println("edit " + userInput.split(" ")[1] + " " + "false");
                    else
                        out.println("edit " + userInput.split(" ")[1] + " " + userInput.split(" ")[2]);
                    continue;
                }

                if (userInput.startsWith("/deleteChat")) {
                    out.println("delete " + userInput.split(" ")[1]);
                    continue;
                }

                if (userInput.startsWith("/add")) {
                    out.println("add " + userInput.split(" ")[1] + " " + userInput.split(" ")[2]);
                    continue;
                }

                if (userInput.startsWith("/remove")) {
                    out.println("remove " + userInput.split(" ")[1] + " " + userInput.split(" ")[2]);
                    continue;
                }

                if (userInput.startsWith("/list")) {
                    out.println("list");
                    continue;
                }

                if (userInput.startsWith("/search")) {
                    out.println("search " + userInput.split(" ")[1]);
                    continue;
                }

                if (userInput.startsWith("/join")) {
                    chatId = Long.parseLong(userInput.split(" ")[1]);
                    out.println("join " + chatId);
                    continue;
                }

                if (userInput.equals("/leave")) {
                    chatId = 0L;
                    System.out.println("You leaved the chat");
                    continue;
                }

                if (chatId == 0) {
                    System.out.println("You are not in a chat, type /help to see the list of commands");
                    continue;
                }

                if (userInput.startsWith("/edit")) {
                    id = Long.parseLong(userInput.split(" ")[1]);
                    userInput = userInput.split(" ")[2];
                }

                if (userInput.startsWith("/delete")) {
                    id = Long.parseLong(userInput.split(" ")[1]);
                    out.println("delete " + id + " " + chatId);
                    continue;
                }

                Message message = Message.builder()
                        .id(id)
                        .text(userInput)
                        .chatId(chatId)
                        .build();
                out.println(message.toJson());
            }

            out.close();
            in.close();
            socket.close();
            System.out.println("Disconnected from the chat server!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}