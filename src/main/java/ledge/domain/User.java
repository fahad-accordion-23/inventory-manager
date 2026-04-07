package ledge.domain;

import java.util.Objects;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String username;
    private final String passwordHash;
    private final Role role;

    public User(UUID id, String username, String passwordHash, Role role) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "PasswordHash cannot be null");
        this.role = Objects.requireNonNull(role, "Role cannot be null");
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

    public Role getRole() {
        return role;
    }
}
