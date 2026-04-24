package ledge.security.api.dto;

import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;

/**
 * Shared DTO for permissions.
 */
public record PermissionDTO(Resource resource, Action action) {
}
