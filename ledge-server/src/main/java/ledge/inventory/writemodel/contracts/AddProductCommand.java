package ledge.inventory.writemodel.contracts;

import ledge.security.api.dto.PermissionDTO;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.shared.infrastructure.commands.Command;

import java.math.BigDecimal;
import java.util.Optional;

public record AddProductCommand(
        String name,
        BigDecimal purchasePrice,
        BigDecimal sellingPrice,
        int stockQuantity,
        BigDecimal taxRate) implements Command {

    private static final PermissionDTO REQUIRED = new PermissionDTO(Resource.PRODUCT, Action.CREATE);

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
