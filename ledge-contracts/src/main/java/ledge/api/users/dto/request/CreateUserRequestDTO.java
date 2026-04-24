package ledge.api.users.dto.request;

import ledge.shared.types.Role;

public record CreateUserRequestDTO(
        String username,
        String password,
        Role role) {}
