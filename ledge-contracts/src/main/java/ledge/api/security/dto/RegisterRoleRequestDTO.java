package ledge.api.security.dto;

import ledge.security.api.dto.PermissionDTO;
import java.util.Set;

/**
 * Request payload to register a new role.
 */
public record RegisterRoleRequestDTO(String name, Set<PermissionDTO> permissions) {
}
