package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    @NotBlank
    private String text;
    @NotNull
    private Long chatId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto author;
    private Boolean isRead = false;
}
