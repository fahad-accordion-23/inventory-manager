package ledge.security.api;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.dto.RoleDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Public interface for managing user roles (Open Host Service).
 * Refined to support exactly one role per user.
 */
public interface IUserRoleService {
    /**
     * Registers a new custom role in the system.
     */
    UUID registerRole(String name, Set<PermissionDTO> permissions);

    /**
     * Retrieves all defined roles.
     */
    List<RoleDTO> getAllRoles();

    /**
     * Retrieves a role by its ID.
     */
    Optional<RoleDTO> getRole(UUID roleId);

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
