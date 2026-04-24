package ledge.security.writemodel.application.services;

import java.util.Set;
import java.util.UUID;

import ledge.security.writemodel.domain.Permission;

/**
 * Interface for the service managing user-role mappings using ID references.
 */
public interface IUserRoleService {
    void assignRole(UUID userId, UUID roleId);

    void removeRole(UUID userId, UUID roleId);

    Set<UUID> getRoleIds(UUID userId);

    boolean hasPermission(UUID userId, Permission permission);
}
