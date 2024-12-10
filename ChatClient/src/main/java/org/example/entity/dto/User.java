package org.example.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private String username;
    private String password;

    public String toJson() {
        return "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
    }
}
