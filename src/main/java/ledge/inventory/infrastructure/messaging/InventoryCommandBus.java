package ledge.inventory.infrastructure.messaging;

import ledge.security.application.services.AuthorizationService;
import ledge.util.cqrs.CommandBus;

public class InventoryCommandBus extends CommandBus {
    public InventoryCommandBus(AuthorizationService authService) {
        super(authService);
    }
}
