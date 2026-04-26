package ledge.security.internal.application;

import ledge.security.api.IPermissionResolver;
import ledge.security.api.dto.PermissionDTO;
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
    private final IPermissionResolver permissionResolver;

    public AuthorizationService(
            ISessionService sessionService, 
            IUserRoleService userRoleService,
            IPermissionResolver permissionResolver) {
        this.sessionService = sessionService;
        this.userRoleService = userRoleService;
        this.permissionResolver = permissionResolver;
    }

    @Override
    public void authorize(String token, Object context) throws AuthorizationException {
        permissionResolver.resolve(context).ifPresent(permission -> authorize(token, permission));
    }

    @Override
    public void authorize(String token, PermissionDTO permissionDTO) throws AuthorizationException {
        if (token == null || token.isBlank()) {
            throw new AuthorizationException("Authentication token is missing");
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
