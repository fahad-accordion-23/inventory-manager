package ledge.inventory.readmodel.infrastructure.messaging;

import ledge.security.application.services.IAuthorizationService;
import ledge.shared.infrastructure.queries.QueryBus;

public class InventoryQueryBus extends QueryBus {
    public InventoryQueryBus(IAuthorizationService authService) {
        super(authService);
    }
}
