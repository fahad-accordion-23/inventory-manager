package ledge.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
}
