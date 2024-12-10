package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@NamedQueries(
        {
                @NamedQuery(
                        name = "findAllByChatId",
                        query = "from Message m JOIN FETCH m.chat JOIN FETCH m.author where m.chat.id = :chatId"
                ),
                @NamedQuery(
                        name = "findByIdAndChatId",
                        query = "from Message m JOIN FETCH m.chat JOIN FETCH m.author where m.id = :id and m.chat.id = :chatId"
                )
        }
)

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @ToString.Exclude
    private User author;
    private String text;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Chat chat;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private Boolean isEdited;
    private Boolean isRead = false;
}
