package ledge.security.application.services;

import ledge.security.application.events.AuthorizationException;
import ledge.security.domain.SessionService;
import ledge.shared.types.Permission;
import ledge.users.application.dtos.UserDTO;

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
     * 
     * @param token      The session token to verify.
     * @param permission The permission required for the action.
     * @throws AuthorizationException if the token is invalid or the user lacks
     *                                permission.
     */
    public void require(String token, Permission permission) throws AuthorizationException, IllegalArgumentException {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Authentication token is missing");
        }

        Optional<UserDTO> userOpt = sessionService.getUserByToken(token);

        if (userOpt.isEmpty()) {
            throw new AuthorizationException("Invalid authentication token");
        }

        UserDTO user = userOpt.get();

        if (permission != null && !user.role().hasPermission(permission)) {
            throw new AuthorizationException(
                    "User " + user.username() + " does not have required permission: " + permission);
        }
    }
}
