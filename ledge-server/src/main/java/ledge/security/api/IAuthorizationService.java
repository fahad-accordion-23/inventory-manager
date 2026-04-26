package ledge.security.api;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.exceptions.AuthorizationException;

/**
 * Public interface for authorization checks (Open Host Service).
 */
public interface IAuthorizationService {
    /**
     * Authorizes the current request context (Command or Query) based on its requirements.
     * 
     * @param token   The session token to verify.
     * @param context The command or query object to authorize.
     * @throws AuthorizationException if the check fails.
     */
    void authorize(String token, Object context) throws AuthorizationException;

    /**
     * Performs a manual authorization check using a specific PermissionDTO.
     * 
     * @param token      The session token to verify.
     * @param permission The required permission.
     * @throws AuthorizationException if the check fails.
     */
    void authorize(String token, PermissionDTO permission) throws AuthorizationException;
}
