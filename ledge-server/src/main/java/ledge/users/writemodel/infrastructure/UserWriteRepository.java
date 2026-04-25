package ledge.users.writemodel.infrastructure;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import ledge.users.writemodel.domain.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class UserWriteRepository implements IUserWriteRepository {
    private static final String DATA_DIR = "data";
    private static final String FILE_PATH = DATA_DIR + "/users.json";
    private final List<User> database = new ArrayList<>();
    private final Gson gson;

    public UserWriteRepository() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        new File(DATA_DIR).mkdirs();
        loadDatabase();
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
    public Optional<User> findById(UUID id) {
        return database.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public void save(User user) {
        database.removeIf(u -> u.getId().equals(user.getId()) || u.getUsername().equals(user.getUsername()));
        database.add(user);
        saveDatabase();
    }

    @Override
    public void delete(UUID id) {
        if (database.removeIf(u -> u.getId().equals(id))) {
            saveDatabase();
        }
    }
}
