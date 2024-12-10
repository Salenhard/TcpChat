package salen.loginService.util;

import org.springframework.data.jpa.domain.Specification;
import salen.loginService.entity.Role;
import salen.loginService.entity.User;

import java.util.List;

public class UserSpecification {
    private UserSpecification() {
    }

    public static Specification<User> likeUsername(String username) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("username"), "%" + username + "%");
    }

    public static Specification<User> isEnabled(Boolean isEnabled) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isEnabled"), isEnabled);
    }

    public static Specification<User> rolesIn(List<Role> roles) {
        return (root, criteriaQuery, criteriaBuilder) ->
                root.get("roles").in(roles);
    }

}
