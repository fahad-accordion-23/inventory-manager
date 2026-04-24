package ledge.inventory.writemodel.contracts;

import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.security.internal.domain.models.Action;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Resource;
import ledge.shared.infrastructure.commands.Command;

import java.math.BigDecimal;
import java.util.Optional;

public record AddProductCommand(
        String name,
        BigDecimal purchasePrice,
        BigDecimal sellingPrice,
        int stockQuantity,
        BigDecimal taxRate) implements Command {

    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.CREATE);

    public AddProductCommand(ProductDTO product) {
        this(product.name(), product.purchasePrice(), product.sellingPrice(), product.stockQuantity(),
                product.taxRate());
    }

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
