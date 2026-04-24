package ledge.security.internal.infrastructure;

import java.util.Set;
import java.util.UUID;

/**
 * Repository interface for storing user-role mappings using ID references.
 */
public interface IUserRoleRepository {
    void saveRoles(UUID userId, Set<UUID> roleIds);

    Set<UUID> findRolesByUserId(UUID userId);

    void deleteRoles(UUID userId);
}
