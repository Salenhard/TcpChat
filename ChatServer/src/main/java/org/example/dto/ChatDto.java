package org.example.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatDto {
    private Long id;
    @NotBlank
    private String name;
    @ToString.Exclude
    private List<UserDto> users;
    private Boolean isPrivate;
    @NotNull
    private UserDto creator;
}
