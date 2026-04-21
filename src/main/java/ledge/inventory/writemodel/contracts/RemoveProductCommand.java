package ledge.inventory.writemodel.contracts;

import ledge.shared.infrastructure.commands.Command;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;

import java.util.Optional;
import java.util.UUID;

public record RemoveProductCommand(UUID productId) implements Command {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.DELETE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
