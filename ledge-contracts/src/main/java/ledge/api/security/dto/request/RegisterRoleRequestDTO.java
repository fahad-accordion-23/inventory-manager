package ledge.api.security.dto.request;

import java.util.List;
import java.util.Map;

import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;

/**
 * Request payload to register a new role.
 * Supports multiple actions per resource.
 */
public record RegisterRoleRequestDTO(String name, Map<Resource, List<Action>> permissions) {
}
