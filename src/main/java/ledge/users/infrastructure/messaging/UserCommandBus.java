package ledge.users.infrastructure.messaging;

import ledge.security.application.services.IAuthorizationService;
import ledge.util.cqrs.CommandBus;

public class UserCommandBus extends CommandBus {
    public UserCommandBus(IAuthorizationService authService) {
        super(authService);
    }
}
