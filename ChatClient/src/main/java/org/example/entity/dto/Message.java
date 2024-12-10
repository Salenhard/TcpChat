package org.example.entity.dto;

import lombok.*;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;
    private String text;
    private Long chatId;

    public String toJson() {
        return "message {" + "\"id\":" + "\"" + id + "\", " + "\"text\":" + "\"" + text + "\"" + ", \"chatId\":" + "\"" + chatId + "\"}";
    }
}
