package ledge.inventory.writemodel.contracts;

import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.shared.infrastructure.commands.Command;
import ledge.security.writemodel.domain.Action;
import ledge.security.writemodel.domain.Permission;
import ledge.security.writemodel.domain.Resource;

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
