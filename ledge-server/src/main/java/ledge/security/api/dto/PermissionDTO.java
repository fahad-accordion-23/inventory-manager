package ledge.security.api.dto;

import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;

/**
 * Public DTO for permissions exposed by the Security OHS.
 * Uses shared enums for type safety.
 */
public record PermissionDTO(Resource resource, Action action) {
}
