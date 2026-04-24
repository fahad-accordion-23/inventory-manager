package ledge.security.internal.infrastructure;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for storing user-to-role mappings using ID references.
 * Each user is restricted to a single role assignment.
 */
public interface IUserRoleRepository {
    /**
     * Persists a role assignment for a user. Overwrites any existing assignment.
     */
    void saveRole(UUID userId, UUID roleId);

    /**
     * Retrieves the role ID assigned to a user.
     */
    Optional<UUID> findRoleByUserId(UUID userId);

    /**
     * Removes the role assignment from a user.
     */
    void deleteRole(UUID userId);
}
