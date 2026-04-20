package ledge.users.infrastructure.messaging;

import ledge.security.application.services.AuthorizationService;
import ledge.util.cqrs.QueryBus;

public class UserQueryBus extends QueryBus {
    public UserQueryBus(AuthorizationService authService) {
        super(authService);
    }
}
