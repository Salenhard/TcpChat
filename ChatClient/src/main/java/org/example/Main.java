package org.example;

import org.example.client.Client;
import org.example.entity.dto.Response;
import org.example.service.LoginService;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LoginService loginService = new LoginService();
        Scanner in = new Scanner(System.in);
        Response verify = new Response();
        String username = "";
        String password;
        Client client;
        while (verify.getCode() != 200) {
            System.out.print("Input username: ");
            username = in.nextLine();
            System.out.print("Input password: ");
            password = in.nextLine();
            try {
                verify = loginService.login(username, password);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println(verify.getMessage());
            }
        }
        client = new Client(username, verify.getMessage());
        client.run();
    }
}