package ledge.inventory.writemodel.contracts;

import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.security.internal.domain.models.Action;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Resource;
import ledge.shared.infrastructure.commands.Command;

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
