package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Chat.deleteAll", query = "DELETE FROM Chat"),
        @NamedQuery(name = "Chat.findAllByLikeNameAndIsPrivate", query = "FROM Chat c WHERE c.name LIKE :name AND c.isPrivate = :isPrivate")
})

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<User> users;
    @OneToMany(orphanRemoval = true, mappedBy = "chat")
    @ToString.Exclude
    private List<Message> messages;
    @Column(columnDefinition = "boolean default true")
    private Boolean isPrivate = true;
    @ManyToOne
    private User creator;
}
