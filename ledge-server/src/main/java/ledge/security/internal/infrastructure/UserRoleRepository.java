package ledge.security.internal.infrastructure;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of IUserRoleRepository restricted to a single role
 * per user.
 */
@Repository
public class UserRoleRepository implements IUserRoleRepository {
    private final Map<UUID, UUID> userRoles = new ConcurrentHashMap<>();

    @Override
    public void saveRole(UUID userId, UUID roleId) {
        if (userId == null || roleId == null) {
            throw new IllegalArgumentException("User ID and role ID cannot be null");
        }
        userRoles.put(userId, roleId);
    }

    @Override
    public Optional<UUID> findRoleByUserId(UUID userId) {
        if (userId == null)
            return Optional.empty();
        return Optional.ofNullable(userRoles.get(userId));
    }

    @Override
    public void deleteRole(UUID userId) {
        if (userId != null) {
            userRoles.remove(userId);
        }
    }
}
