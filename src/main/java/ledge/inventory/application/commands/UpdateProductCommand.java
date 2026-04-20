package ledge.inventory.application.commands;

import ledge.inventory.application.dtos.ProductDTO;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;
import ledge.util.cqrs.Command;
import java.util.Optional;

public record UpdateProductCommand(ProductDTO product) implements Command {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.UPDATE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
