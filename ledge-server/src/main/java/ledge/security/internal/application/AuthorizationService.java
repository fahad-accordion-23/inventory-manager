package ledge.security.internal.application;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;
import ledge.security.api.exceptions.AuthorizationException;
import ledge.security.api.IAuthorizationService;
import ledge.security.api.IUserRoleService;
import ledge.security.internal.domain.services.ISessionService;

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

    @Override
    public void require(String token, PermissionDTO permissionDTO)
            throws AuthorizationException, IllegalArgumentException {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Authentication token is missing");
        }

        UUID userId = sessionService.getUserIdByToken(token)
                .orElseThrow(() -> new AuthorizationException("Invalid authentication token"));

        if (permissionDTO != null) {
            if (!userRoleService.hasPermission(userId, permissionDTO)) {
                throw new AuthorizationException(
                        "User does not have required permission: " + permissionDTO);
            }
        }
    }
}
