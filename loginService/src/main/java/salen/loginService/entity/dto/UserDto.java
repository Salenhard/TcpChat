package salen.loginService.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import salen.loginService.entity.Role;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    @Size(min = 3)
    String username;
    @NotBlank
    @Size(min = 8)
    String password;
    @NotBlank
    Set<Role> roles;
}