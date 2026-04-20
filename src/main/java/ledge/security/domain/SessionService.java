package ledge.security.domain;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ledge.users.application.dtos.UserDTO;

/**
 * Service for managing security tokens and sessions.
 * Provides functionality to create, delete, and look up tokens.
 */
public class SessionService implements ISessionService {
    private final Map<String, UserDTO> sessions = new ConcurrentHashMap<>();

    /**
     * Creates a new unique token for a user and stores it.
     * 
     * @param userId The user for whom the token is created.
     * @return The generated unique token string.
     */
    public String createToken(UserDTO user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        String token = UUID.randomUUID().toString();
        sessions.put(token, user);
        return token;
    }

    /**
     * Removes an existing token from the active sessions.
     * 
     * @param token The token string to remove.
     */
    public void removeToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }

        sessions.remove(token);
    }

    /**
     * Retrieves the user associated with the given token.
     * 
     * @param token The token string.
     * @return An Optional containing the user if found, or empty otherwise.
     */
    public Optional<UserDTO> getUserByToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }

        return Optional.ofNullable(sessions.get(token));
    }
}
