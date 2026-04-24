package ledge.users.readmodel.infrastructure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ledge.users.readmodel.dtos.UserDTO;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

/**
 * Implementation of UserReadRepository optimized for high-read performance.
 * Maintains a secondary index for usernames to ensure O(1) lookups by username.
 * Initializes data from a JSON file.
 */
@Repository
public class UserReadRepository implements IUserReadRepository {

    private final Map<UUID, UserDTO> usersById = new ConcurrentHashMap<>();
    private final Map<String, UUID> idByUsername = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public UserReadRepository() {
        loadInitialData();
    }

    private void loadInitialData() {
        try (FileReader reader = new FileReader("data/users.json")) {
            Type listType = new TypeToken<List<UserDTO>>() {
            }.getType();
            List<UserDTO> initialUsers = gson.fromJson(reader, listType);
            if (initialUsers != null) {
                for (UserDTO user : initialUsers) {
                    save(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load initial users from data/users.json: " + e.getMessage());
        }
    }

    @Override
    public Optional<UserDTO> findById(UUID id) {
        if (id == null)
            return Optional.empty();
        return Optional.ofNullable(usersById.get(id));
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        if (username == null)
            return Optional.empty();
        UUID id = idByUsername.get(username);
        if (id == null)
            return Optional.empty();
        return findById(id);
    }

    @Override
    public List<UserDTO> findAll() {
        return new ArrayList<>(usersById.values());
    }

    @Override
    public void save(UserDTO user) {
        if (user == null || user.id() == null)
            return;

        // Clean up old username index if username changed or user is being updated
        UserDTO existing = usersById.get(user.id());
        if (existing != null && !existing.username().equals(user.username())) {
            idByUsername.remove(existing.username());
        }

        usersById.put(user.id(), user);
        idByUsername.put(user.username(), user.id());
    }

    @Override
    public void deleteById(UUID id) {
        if (id == null)
            return;
        UserDTO user = usersById.remove(id);
        if (user != null) {
            idByUsername.remove(user.username());
        }
    }

}
