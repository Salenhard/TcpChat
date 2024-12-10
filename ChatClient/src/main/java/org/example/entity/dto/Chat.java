package org.example.entity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    private Long id;
    private String name;
    private Boolean isPrivate = true;
    public String toJson()
    {
        return "{" + "\"id\":\"" + id + "\", \"name\":\"" + name + "\", \"isPrivate\":\"" + isPrivate + "\"}";
    }
}
