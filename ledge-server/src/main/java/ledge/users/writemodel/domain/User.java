package ledge.users.writemodel.domain;

import java.util.Objects;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String passwordHash;

    private User(UUID id, String username, String passwordHash) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "PasswordHash cannot be null");
    }

    public static User register(String username, String passwordHash) {
        return new User(UUID.randomUUID(), username, passwordHash);
    }

    public static User rehydrate(UUID id, String username, String passwordHash) {
        return new User(id, username, passwordHash);
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
