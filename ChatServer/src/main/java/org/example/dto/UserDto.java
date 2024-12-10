package org.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDto {
    @NotBlank
    private String username;
    @ToString.Exclude
    private String password;

    public UserDto(String username) {
        this.username = username;
    }
}
