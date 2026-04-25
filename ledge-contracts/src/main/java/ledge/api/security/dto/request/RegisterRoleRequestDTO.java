package ledge.api.security.dto.request;

import java.util.Map;

import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;

/**
 * Request payload to register a new role.
 */
public record RegisterRoleRequestDTO(String name, Map<Resource, Action> permissions) {
}
