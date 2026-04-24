package ledge.inventory.writemodel.contracts;

import ledge.security.internal.domain.models.Action;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Resource;
import ledge.shared.infrastructure.commands.Command;

import java.util.Optional;
import java.util.UUID;

public record RemoveProductCommand(UUID productId) implements Command {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.DELETE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
