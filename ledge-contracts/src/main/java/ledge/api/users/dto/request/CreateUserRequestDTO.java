package ledge.api.users.dto.request;

/**
 * Request payload to create a new user.
 * Roles are managed separately via the Security OHS endpoints.
 */
public record CreateUserRequestDTO(
        String username,
        String password) {}
