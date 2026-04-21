package ledge.api.users.dto.response;

import ledge.shared.types.Role;
import java.util.UUID;

/**
 * Standard user data transfer object for API responses.
 */
public record UserResponseDTO(
        UUID id,
        String username,
        Role role) {}
