package ledge.inventory.writemodel.contracts;

import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.shared.infrastructure.commands.Command;
import ledge.security.writemodel.domain.Action;
import ledge.security.writemodel.domain.Permission;
import ledge.security.writemodel.domain.Resource;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record UpdateProductCommand(UUID id, String name, BigDecimal purchasePrice, BigDecimal sellingPrice,
        int stockQuantity, BigDecimal taxRate) implements Command {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.UPDATE);

    public UpdateProductCommand(ProductDTO dto) {
        this(dto.id(), dto.name(), dto.purchasePrice(), dto.sellingPrice(), dto.stockQuantity(), dto.taxRate());
    }

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
