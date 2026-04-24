package ledge.users.writemodel.infrastructure.messaging;

import ledge.security.application.services.IAuthorizationService;
import ledge.shared.infrastructure.commands.CommandBus;

public class UserCommandBus extends CommandBus {
    public UserCommandBus(IAuthorizationService authService) {
        super(authService);
    }
}
