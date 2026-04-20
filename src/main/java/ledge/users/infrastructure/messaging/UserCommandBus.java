package ledge.users.infrastructure.messaging;

import ledge.security.application.services.AuthorizationService;
import ledge.util.cqrs.CommandBus;

public class UserCommandBus extends CommandBus {
    public UserCommandBus(AuthorizationService authService) {
        super(authService);
    }
}
