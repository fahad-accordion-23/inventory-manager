package ledge.domain;

import java.util.HashSet;
import java.util.Set;

public final class Roles {

    public static final Role ADMIN;
    public static final Role INVENTORY_MANAGER;
    public static final Role SALES_STAFF;

    static {
        Set<Permission> adminPerms = new HashSet<>();
        for (Resource r : Resource.values()) {
            for (Action a : Action.values()) {
                adminPerms.add(new Permission(r, a));
            }
        }
        ADMIN = new Role("ADMIN", adminPerms);

        Set<Permission> managerPerms = new HashSet<>();
        managerPerms.add(new Permission(Resource.PRODUCT, Action.CREATE));
        managerPerms.add(new Permission(Resource.PRODUCT, Action.READ));
        managerPerms.add(new Permission(Resource.PRODUCT, Action.UPDATE));
        managerPerms.add(new Permission(Resource.PRODUCT, Action.DELETE));
        INVENTORY_MANAGER = new Role("INVENTORY_MANAGER", managerPerms);

        Set<Permission> salesPerms = new HashSet<>();
        salesPerms.add(new Permission(Resource.PRODUCT, Action.READ));
        salesPerms.add(new Permission(Resource.INVOICE, Action.CREATE));
        SALES_STAFF = new Role("SALES_STAFF", salesPerms);
    }

    private Roles() {
        // Utility class
    }
}
