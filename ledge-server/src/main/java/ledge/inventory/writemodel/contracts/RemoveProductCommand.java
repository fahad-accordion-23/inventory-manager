package ledge.inventory.writemodel.contracts;

import ledge.security.api.annotations.RequiresPermission;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.shared.infrastructure.commands.Command;
import java.util.UUID;

@RequiresPermission(resource = Resource.PRODUCT, action = Action.DELETE)
public record RemoveProductCommand(UUID productId) implements Command {
}
