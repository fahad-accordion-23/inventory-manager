package ledge.security.api.dto;

import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;

/**
 * Public DTO for permissions exposed by the Security OHS.
 * Uses shared enums for type safety.
 */
public record PermissionDTO(Resource resource, Action action) {
}
