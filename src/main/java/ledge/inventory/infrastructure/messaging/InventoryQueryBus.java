package ledge.inventory.infrastructure.messaging;

import ledge.security.application.services.AuthorizationService;
import ledge.util.cqrs.QueryBus;

public class InventoryQueryBus extends QueryBus {
    public InventoryQueryBus(AuthorizationService authService) {
        super(authService);
    }
}
