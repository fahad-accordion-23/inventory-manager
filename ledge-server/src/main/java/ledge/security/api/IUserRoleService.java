package ledge.security.api;

import ledge.security.api.dto.PermissionDTO;

import java.util.Optional;
import java.util.UUID;

/**
 * Public interface for managing user-to-role assignments (Open Host Service).
 * Role management itself is now handled by IRoleService.
 */
public interface IUserRoleService {
    /**
     * Assigns a role to a user, replacing any previously assigned role.
     */
    void assignRole(UUID userId, UUID roleId);

    /**
     * Removes the role assignment from a user.
     */
    void removeRole(UUID userId);

    /**
     * Retrieves the ID of the role assigned to the user.
     */
    Optional<UUID> getRoleId(UUID userId);

    /**
     * Checks if a user has a specific permission based on their assigned role.
     */
    boolean hasPermission(UUID userId, PermissionDTO permission);
}
