package salen.loginService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
public class User {
    @Id
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "username"))
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
    private boolean isEnabled;
}
