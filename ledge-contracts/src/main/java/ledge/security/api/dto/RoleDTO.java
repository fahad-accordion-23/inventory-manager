package ledge.security.api.dto;

import java.util.Set;
import java.util.UUID;

/**
 * Shared DTO for roles.
 */
public record RoleDTO(UUID id, String name, Set<PermissionDTO> permissions) {
}
