package ledge.security.internal.infrastructure;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of IUserRoleRepository using ID references.
 */
@Repository
public class UserRoleRepository implements IUserRoleRepository {
    private final Map<UUID, Set<UUID>> userRoles = new ConcurrentHashMap<>();

    @Override
    public void saveRoles(UUID userId, Set<UUID> roleIds) {
        if (userId == null || roleIds == null) {
            throw new IllegalArgumentException("User ID and role IDs cannot be null");
        }
        userRoles.put(userId, new HashSet<>(roleIds));
    }

    @Override
    public Set<UUID> findRolesByUserId(UUID userId) {
        return userRoles.getOrDefault(userId, Collections.emptySet());
    }

    @Override
    public void deleteRoles(UUID userId) {
        userRoles.remove(userId);
    }
}
