package ledge.security.application;

import ledge.security.domain.User;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing security tokens and sessions.
 * Provides functionality to create, delete, and look up tokens.
 */
public class SessionService {
    private final Map<String, User> sessions = new ConcurrentHashMap<>();

    /**
     * Creates a new unique token for a user and stores it.
     * @param user The user for whom the token is created.
     * @return The generated unique token string.
     */
    public String createToken(User user) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, user);
        return token;
    }

    /**
     * Removes an existing token from the active sessions.
     * @param token The token string to remove.
     */
    public void removeToken(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }

    /**
     * Retrieves the user associated with the given token.
     * @param token The token string.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    public Optional<User> getUserByToken(String token) {
        if (token == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(sessions.get(token));
    }
}
