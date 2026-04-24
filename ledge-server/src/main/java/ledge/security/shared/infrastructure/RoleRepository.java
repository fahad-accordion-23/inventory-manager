package ledge.security.shared.infrastructure;

import ledge.security.writemodel.domain.*;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-memory repository for user roles.
 */
@Repository
public class RoleRepository implements IRoleRepository {
    private final Map<String, Role> roles = new HashMap<>();

    public RoleRepository() {
        seedRoles();
    }

    private void seedRoles() {
        // ADMIN
        Set<Permission> adminPerms = new HashSet<>();
        for (Resource r : Resource.values()) {
            for (Action a : Action.values()) {
                adminPerms.add(new Permission(r, a));
            }
        }
        save(new Role("ADMIN", adminPerms));

        // INVENTORY_MANAGER
        save(new Role("INVENTORY_MANAGER", Set.of(
                new Permission(Resource.PRODUCT, Action.CREATE),
                new Permission(Resource.PRODUCT, Action.READ),
                new Permission(Resource.PRODUCT, Action.UPDATE),
                new Permission(Resource.PRODUCT, Action.DELETE))));

        // SALES_STAFF
        save(new Role("SALES_STAFF", Set.of(
                new Permission(Resource.PRODUCT, Action.READ),
                new Permission(Resource.INVOICE, Action.CREATE))));
    }

    @Override
    public Optional<Role> findByName(String name) {
        return Optional.ofNullable(roles.get(name));
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(roles.values());
    }

    @Override
    public void save(Role role) {
        roles.put(role.getName(), role);
    }
}
