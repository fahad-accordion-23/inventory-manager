package ledge.application.command;

import ledge.application.dto.ProductDTO;
import ledge.security.Action;
import ledge.security.Permission;
import ledge.security.Resource;
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
