package ledge.users.application.dtos;

import ledge.shared.types.Role;
import ledge.users.domain.User;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        Role role) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRole());
    }
}
