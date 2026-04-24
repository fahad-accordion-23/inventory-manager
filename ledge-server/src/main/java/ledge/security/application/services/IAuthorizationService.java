package ledge.security.application.services;

import ledge.security.application.events.AuthorizationException;
import ledge.shared.types.Permission;

public interface IAuthorizationService {
    void require(String token, Permission permission) throws AuthorizationException, IllegalArgumentException;
}
