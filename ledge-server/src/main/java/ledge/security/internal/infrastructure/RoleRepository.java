package ledge.security.internal.infrastructure;

import org.springframework.stereotype.Repository;

import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;
import ledge.security.internal.domain.models.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for user roles.
 */
@Repository
public class RoleRepository implements IRoleRepository {
    private final Map<UUID, Role> rolesById = new ConcurrentHashMap<>();

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
        save(Role.register("ADMIN", adminPerms));

        // INVENTORY_MANAGER
        save(Role.register("INVENTORY_MANAGER", Set.of(
                new Permission(Resource.PRODUCT, Action.CREATE),
                new Permission(Resource.PRODUCT, Action.READ),
                new Permission(Resource.PRODUCT, Action.UPDATE),
                new Permission(Resource.PRODUCT, Action.DELETE))));

        // SALES_STAFF
        save(Role.register("SALES_STAFF", Set.of(
                new Permission(Resource.PRODUCT, Action.READ),
                new Permission(Resource.INVOICE, Action.CREATE))));

        // DEFAULT_USER
        save(Role.register("DEFAULT_USER", Set.of(
                new Permission(Resource.PRODUCT, Action.READ))));
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return Optional.ofNullable(rolesById.get(id));
    }

    @Override
    public Optional<Role> findByName(String name) {
        return rolesById.values().stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(rolesById.values());
    }

    @Override
    public void save(Role role) {
        rolesById.put(role.getId(), role);
    }
}
