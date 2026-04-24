package ledge.users.readmodel.infrastructure.messaging;

import ledge.security.application.services.IAuthorizationService;
import ledge.shared.infrastructure.queries.QueryBus;

public class UserQueryBus extends QueryBus {
    public UserQueryBus(IAuthorizationService authService) {
        super(authService);
    }
}
