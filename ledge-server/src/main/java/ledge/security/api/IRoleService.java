package ledge.security.api;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.dto.RoleDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Public interface for managing system roles.
 */
public interface IRoleService {
    /**
     * Registers a new role with a set of permissions.
     */
    UUID registerRole(String name, Set<PermissionDTO> permissions);

    /**
     * Retrieves all roles.
     */
    List<RoleDTO> getAllRoles();

    /**
     * Retrieves a role by ID.
     */
    Optional<RoleDTO> getRole(UUID roleId);

    /**
     * Retrieves a role by its unique name.
     */
    Optional<RoleDTO> getRoleByName(String name);

    /**
     * Deletes a role. This should fail if the role is still assigned to users.
     */
    void deleteRole(UUID roleId);
}
