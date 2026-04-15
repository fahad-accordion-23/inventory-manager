package ledge.application.command;

import ledge.security.Action;
import ledge.security.Permission;
import ledge.security.Resource;
import ledge.util.cqrs.Command;
import java.util.Optional;
import java.util.UUID;

public class RemoveProductCommand implements Command {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.DELETE);

    private final UUID productId;

    public RemoveProductCommand(UUID productId) {
        this.productId = productId;
    }

    public UUID getProductId() {
        return productId;
    }

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
