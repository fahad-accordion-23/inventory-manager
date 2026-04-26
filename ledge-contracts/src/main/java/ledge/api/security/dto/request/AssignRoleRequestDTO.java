package ledge.api.security.dto.request;

import java.util.UUID;

/**
 * Request payload to assign a role to a user.
 */
public record AssignRoleRequestDTO(UUID roleId) {
}
