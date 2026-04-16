package ledge.infrastructure;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import ledge.util.PasswordHasher;
import ledge.security.domain.Role;
import ledge.security.domain.Roles;
import ledge.security.domain.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserJsonRepository implements UserRepository {
    private static final String DATA_DIR = System.getProperty("user.dir") + "/data";
    private static final String FILE_PATH = DATA_DIR + "/users.json";
    private final List<User> database = new ArrayList<>();
    private final Gson gson;

    public UserJsonRepository() {
        // Register custom TypeAdapter for Role to save/load just the role name
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Role.class, new RoleTypeAdapter())
                .setPrettyPrinting()
                .create();

        new File(DATA_DIR).mkdirs();
        loadDatabase();

        if (database.isEmpty()) {
            seedDatabase();
        }
    }

    private void seedDatabase() {
        save(new User(UUID.randomUUID(), "admin", PasswordHasher.hash("admin123"), Roles.ADMIN));
    }

    private void loadDatabase() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type listType = new TypeToken<ArrayList<User>>() {
                }.getType();
                List<User> loadedUsers = gson.fromJson(reader, listType);
                if (loadedUsers != null) {
                    database.addAll(loadedUsers);
                }
            } catch (IOException e) {
                System.err.println("Failed to load user database: " + e.getMessage());
            }
        }
    }

    private void saveDatabase() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(database, writer);
        } catch (IOException e) {
            System.err.println("Failed to save user database: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return database.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public void save(User user) {
        database.removeIf(u -> u.getId().equals(user.getId()) || u.getUsername().equals(user.getUsername()));
        database.add(user);
        saveDatabase();
    }

    // Custom TypeAdapter to serialize Role as string and deserialize back to static
    // instances
    private static class RoleTypeAdapter implements JsonSerializer<Role>, JsonDeserializer<Role> {
        @Override
        public JsonElement serialize(Role src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getName());
        }

        @Override
        public Role deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String roleName = json.getAsString();
            Role role = Roles.BY_NAME.get(roleName);
            if (role == null) {
                throw new JsonParseException("Unknown role: " + roleName);
            }
            return role;
        }
    }
}
