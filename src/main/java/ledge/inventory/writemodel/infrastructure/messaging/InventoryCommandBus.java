package ledge.inventory.writemodel.infrastructure.messaging;

import ledge.security.application.services.IAuthorizationService;
import ledge.shared.infrastructure.commands.CommandBus;

public class InventoryCommandBus extends CommandBus {
    public InventoryCommandBus(IAuthorizationService authService) {
        super(authService);
    }
}
