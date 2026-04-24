package ledge.inventory.writemodel.contracts;

import ledge.shared.infrastructure.commands.Command;
import ledge.security.writemodel.domain.Action;
import ledge.security.writemodel.domain.Permission;
import ledge.security.writemodel.domain.Resource;

import java.util.Optional;
import java.util.UUID;

public record RemoveProductCommand(UUID productId) implements Command {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.DELETE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
