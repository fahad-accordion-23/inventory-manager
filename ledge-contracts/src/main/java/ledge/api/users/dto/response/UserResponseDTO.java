package ledge.api.users.dto.response;

import java.util.UUID;
import ledge.security.api.dto.RoleDTO;

/**
 * Standard user data transfer object for API responses.
 * Includes the full role details for client-side permission handling.
 */
public record UserResponseDTO(
        UUID id,
        String username,
        RoleDTO role) {}
