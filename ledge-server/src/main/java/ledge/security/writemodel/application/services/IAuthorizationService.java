package ledge.security.writemodel.application.services;

import ledge.security.writemodel.application.events.AuthorizationException;
import ledge.security.writemodel.domain.Permission;

public interface IAuthorizationService {
    void require(String token, Permission permission) throws AuthorizationException, IllegalArgumentException;
}
