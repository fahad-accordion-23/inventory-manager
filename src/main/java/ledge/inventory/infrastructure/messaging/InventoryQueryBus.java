package ledge.inventory.infrastructure.messaging;

import ledge.security.application.services.IAuthorizationService;
import ledge.util.cqrs.QueryBus;

public class InventoryQueryBus extends QueryBus {
    public InventoryQueryBus(IAuthorizationService authService) {
        super(authService);
    }
}
