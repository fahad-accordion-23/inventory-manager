package ledge.shared.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum Role {

    ADMIN(allPermissions()),

    INVENTORY_MANAGER(Set.of(
            perm(Resource.PRODUCT, Action.CREATE),
            perm(Resource.PRODUCT, Action.READ),
            perm(Resource.PRODUCT, Action.UPDATE),
            perm(Resource.PRODUCT, Action.DELETE))),

    SALES_STAFF(Set.of(
            perm(Resource.PRODUCT, Action.READ),
            perm(Resource.INVOICE, Action.CREATE)));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = Collections.unmodifiableSet(new HashSet<>(permissions));
    }

    private static Permission perm(Resource r, Action a) {
        return new Permission(r, a);
    }

    private static Set<Permission> allPermissions() {
        Set<Permission> perms = new HashSet<>();
        for (Resource r : Resource.values()) {
            for (Action a : Action.values()) {
                perms.add(perm(r, a));
            }
        }
        return perms;
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
}