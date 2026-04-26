package ledge.security.internal.infrastructure;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
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
        if (!dir.exists())
            dir.mkdirs();

        load();
    }

    private void load() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<List<Role>>() {
                }.getType();
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

    @Override
    public void delete(UUID id) {
        if (database.remove(id) != null) {
            persist();
        }
    }
}
