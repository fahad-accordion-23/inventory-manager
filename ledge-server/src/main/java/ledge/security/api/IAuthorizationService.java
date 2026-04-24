package ledge.security.api;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.exceptions.AuthorizationException;

/**
 * Public interface for authorization checks (Open Host Service).
 */
public interface IAuthorizationService {
    /**
     * Enforces a permission check for the given token.
     * 
     * @param token      The session token to verify.
     * @param permission The permission required for the action (as DTO).
     * @throws AuthorizationException if the check fails.
     */
    void require(String token, PermissionDTO permission) throws AuthorizationException, IllegalArgumentException;
}
