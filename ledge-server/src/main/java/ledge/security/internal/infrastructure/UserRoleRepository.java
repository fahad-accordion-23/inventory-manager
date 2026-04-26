package ledge.security.internal.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSON-based persistent repository for user-role assignments.
 */
@Repository
@Primary
public class UserRoleRepository implements IUserRoleRepository {
    private static final String DATA_DIR = "data";
    private static final String FILE_PATH = DATA_DIR + "/user_roles.json";
    private final Map<UUID, UUID> database = new ConcurrentHashMap<>();
    private final Gson gson;

    public UserRoleRepository() {
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
                Type type = new TypeToken<Map<UUID, UUID>>() {
                }.getType();
                Map<UUID, UUID> loaded = gson.fromJson(reader, type);
                if (loaded != null) {
                    database.putAll(loaded);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void persist() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(database, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveRole(UUID userId, UUID roleId) {
        database.put(userId, roleId);
        persist();
    }

    @Override
    public Optional<UUID> findRoleByUserId(UUID userId) {
        return Optional.ofNullable(database.get(userId));
    }

    @Override
    public void deleteRole(UUID userId) {
        if (database.remove(userId) != null) {
            persist();
        }
    }

    @Override
    public boolean isRoleAssigned(UUID roleId) {
        return database.values().contains(roleId);
    }
}
