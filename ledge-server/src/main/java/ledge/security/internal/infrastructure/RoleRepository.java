package ledge.security.internal.infrastructure;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Role;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSON-based persistent repository for roles.
 */
@Repository
@Primary
public class RoleRepository implements IRoleRepository {
    private static final String DATA_DIR = "data";
    private static final String FILE_PATH = DATA_DIR + "/roles.json";
    private final Map<UUID, Role> database = new ConcurrentHashMap<>();
    private final Gson gson;

    public RoleRepository() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();

        load();
        if (database.isEmpty()) {
            seed();
        }
    }

    private void seed() {
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

    private void load() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<List<Role>>() {}.getType();
                List<Role> roles = gson.fromJson(reader, type);
                if (roles != null) {
                    for (Role role : roles) {
                        database.put(role.getId(), role);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void persist() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(new ArrayList<>(database.values()), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<Role> findByName(String name) {
        return database.values().stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void save(Role role) {
        database.put(role.getId(), role);
        persist();
    }
}
