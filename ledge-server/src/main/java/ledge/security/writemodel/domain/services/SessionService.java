package ledge.security.writemodel.domain.services;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Service for managing security tokens and sessions.
 * Maps tokens against user IDs.
 */
@Service
public class SessionService implements ISessionService {
    private final Map<String, UUID> sessions = new ConcurrentHashMap<>();

    /**
     * Creates a new unique token for a user and stores it.
     * 
     * @param userId The ID of the user for whom the token is created.
     * @return The generated unique token string.
     */
    @Override
    public String createToken(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        String token = UUID.randomUUID().toString();
        sessions.put(token, userId);
        return token;
    }

    /**
     * Removes an existing token from the active sessions.
     * 
     * @param token The token string to remove.
     */
    @Override
    public void removeToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }

        sessions.remove(token);
    }

    /**
     * Retrieves the user ID associated with the given token.
     * 
     * @param token The token string.
     * @return An Optional containing the user ID if found, or empty otherwise.
     */
    @Override
    public Optional<UUID> getUserIdByToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }

        return Optional.ofNullable(sessions.get(token));
    }
}
