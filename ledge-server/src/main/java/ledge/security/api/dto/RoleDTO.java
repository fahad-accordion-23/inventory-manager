package ledge.security.api.dto;

import java.util.Set;
import java.util.UUID;

/**
 * Public DTO for roles exposed by the Security OHS.
 */
public record RoleDTO(UUID id, String name, Set<PermissionDTO> permissions) {
}
