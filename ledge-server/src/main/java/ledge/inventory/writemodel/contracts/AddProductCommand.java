package ledge.inventory.writemodel.contracts;

import ledge.security.api.annotations.RequiresPermission;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.shared.infrastructure.commands.Command;

import java.math.BigDecimal;

@RequiresPermission(resource = Resource.PRODUCT, action = Action.CREATE)
public record AddProductCommand(
        String name,
        BigDecimal purchasePrice,
        BigDecimal sellingPrice,
        int stockQuantity,
        BigDecimal taxRate) implements Command {
}
