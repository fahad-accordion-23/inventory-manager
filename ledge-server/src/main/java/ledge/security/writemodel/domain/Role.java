package ledge.security.writemodel.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain model representing a user role with a set of associated permissions.
 */
public class Role {
    private final String name;
    private final Set<Permission> permissions;

    public Role(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = Collections.unmodifiableSet(new HashSet<>(permissions));
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
