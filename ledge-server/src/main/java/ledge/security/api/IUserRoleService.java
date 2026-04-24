package ledge.security.api;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;
import java.util.Set;
import java.util.UUID;

/**
 * Public interface for managing user roles (Open Host Service).
 */
public interface IUserRoleService {
    /**
     * Registers a new custom role in the system.
     * 
     * @param name        The unique name of the role.
     * @param permissions The set of permissions to associate with this role.
     * @return The UUID of the newly created role.
     */
    UUID registerRole(String name, Set<PermissionDTO> permissions);

    void assignRole(UUID userId, UUID roleId);

    void removeRole(UUID userId, UUID roleId);

    Set<UUID> getRoleIds(UUID userId);

    boolean hasPermission(UUID userId, PermissionDTO permission);
}
