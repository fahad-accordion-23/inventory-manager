package ledge.inventory.infrastructure.messaging;

import ledge.security.application.services.IAuthorizationService;
import ledge.util.cqrs.CommandBus;

public class InventoryCommandBus extends CommandBus {
    public InventoryCommandBus(IAuthorizationService authService) {
        super(authService);
    }
}
