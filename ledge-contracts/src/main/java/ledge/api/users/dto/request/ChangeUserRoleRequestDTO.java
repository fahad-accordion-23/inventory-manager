package ledge.api.users.dto.request;

import java.util.UUID;

/**
 * Request payload to update a user's role.
 * Note: This functionality is primarily moved to the Security OHS (AssignRoleRequestDTO).
 */
public record ChangeUserRoleRequestDTO(
        UUID userId,
        UUID roleId) {}
