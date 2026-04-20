package ledge.users.infrastructure.messaging;

import ledge.security.application.services.IAuthorizationService;
import ledge.util.cqrs.QueryBus;

public class UserQueryBus extends QueryBus {
    public UserQueryBus(IAuthorizationService authService) {
        super(authService);
    }
}
