package ledge.security.api.dto;

/**
 * Public DTO for permissions exposed by the Security OHS.
 */
public record PermissionDTO(String resource, String action) {
}
