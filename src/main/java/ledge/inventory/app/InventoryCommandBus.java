package ledge.inventory.app;

import ledge.security.app.AccessPolicy;
import ledge.util.cqrs.Command;
import ledge.util.cqrs.CommandBus;

public class InventoryCommandBus extends CommandBus {

    @Override
    public void dispatch(Command command) {
        command.getRequiredPermission().ifPresent(AccessPolicy::require);
        super.dispatch(command);
    }
}
