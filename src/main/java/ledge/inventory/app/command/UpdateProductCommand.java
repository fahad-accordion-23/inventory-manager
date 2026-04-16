package ledge.inventory.app.command;

import ledge.inventory.app.dto.ProductDTO;
import ledge.security.domain.Action;
import ledge.security.domain.Permission;
import ledge.security.domain.Resource;
import ledge.util.cqrs.Command;
import java.util.Optional;

public class UpdateProductCommand implements Command {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.UPDATE);

    private final ProductDTO product;

    public UpdateProductCommand(ProductDTO product) {
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
