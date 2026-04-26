package ledge.security.api;

import ledge.security.api.exceptions.AuthenticationException;
import java.util.Optional;
import java.util.UUID;

/**
 * Open Host Service (OHS) for the Security bounded context.
 */
public interface IAuthenticationService {
    
    /**
     * Authenticates a user and returns a session token.
     */
    String login(String username, String password) throws AuthenticationException;

    /**
     * Terminates the session associated with the given token.
     */
    void logout(String token);

    /**
     * Resolves a user ID from a valid session token.
     */
    Optional<UUID> getUserIdByToken(String token);
}
