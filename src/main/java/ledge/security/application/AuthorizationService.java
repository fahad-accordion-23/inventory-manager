package ledge.security.application;

import ledge.security.application.events.AuthorizationException;
import ledge.security.domain.Permission;
import ledge.security.domain.User;
import java.util.Optional;

/**
 * Service for enforcing authorization rules based on session tokens.
 */
public class AuthorizationService {
    private final SessionService sessionService;

    public AuthorizationService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Enforces a permission check for the given token.
     * @param token The session token to verify.
     * @param permission The permission required for the action.
     * @throws AuthorizationException if the token is invalid or the user lacks permission.
     */
    public void require(String token, Permission permission) throws AuthorizationException {
        if (token == null) {
            throw new AuthorizationException("Authentication token is missing");
        }

        Optional<User> userOpt = sessionService.getUserByToken(token);
        if (userOpt.isEmpty()) {
            throw new AuthorizationException("Invalid or expired session token");
        }

        if (permission != null && !userOpt.get().getRole().hasPermission(permission)) {
            throw new AuthorizationException("User " + userOpt.get().getUsername() + 
                " does not have required permission: " + permission);
        }
    }
}
