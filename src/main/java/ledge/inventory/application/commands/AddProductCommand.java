package ledge.inventory.application.commands;

import ledge.inventory.application.dtos.ProductDTO;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;
import ledge.util.cqrs.Command;
import java.util.Optional;

public class AddProductCommand implements Command {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.CREATE);

    private final ProductDTO product;

    public AddProductCommand(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {
        return product;
    }

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
