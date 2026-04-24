package ledge.security.internal.domain.models;

import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Domain model representing a user role with a set of associated permissions.
 */
public class Role {
    private final UUID id;
    private final String name;
    private final Set<Permission> permissions;

    private Role(UUID id, String name, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = Collections.unmodifiableSet(new HashSet<>(permissions));
    }

    /**
     * Creates a new role with a random UUID.
     */
    public static Role register(String name, Set<Permission> permissions) {
        return new Role(UUID.randomUUID(), name, permissions);
    }

    /**
     * Reconstitutes an existing role with a specific UUID.
     */
    public static Role rehydrate(UUID id, String name, Set<Permission> permissions) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null when rehydrating a role");
        }
        return new Role(id, name, permissions);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(Resource resource, Action action) {
        return permissions.contains(new Permission(resource, action));
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Role role = (Role) o;
        return id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
