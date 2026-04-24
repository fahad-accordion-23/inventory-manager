package ledge.security.writemodel.application.services;

import ledge.security.writemodel.application.events.AuthorizationException;
import ledge.security.writemodel.domain.Permission;
import ledge.security.writemodel.domain.services.ISessionService;

import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * Service responsible for evaluating permissions against a user's token.
 */
@Service
public class AuthorizationService implements IAuthorizationService {
    private final ISessionService sessionService;
    private final IUserRoleService userRoleService;

    public AuthorizationService(ISessionService sessionService, IUserRoleService userRoleService) {
        this.sessionService = sessionService;
        this.userRoleService = userRoleService;
    }

    /**
     * Enforces a permission check for the given token.
     * 
     * @param token      The session token to verify.
     * @param permission The permission required for the action.
     * @throws AuthorizationException if the token is invalid or the user lacks
     *                                permission.
     */
    @Override
    public void require(String token, Permission permission) throws AuthorizationException, IllegalArgumentException {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Authentication token is missing");
        }

        UUID userId = sessionService.getUserIdByToken(token)
                .orElseThrow(() -> new AuthorizationException("Invalid authentication token"));

        if (permission != null && !userRoleService.hasPermission(userId, permission)) {
            throw new AuthorizationException(
                    "User does not have required permission: " + permission);
        }
    }
}
