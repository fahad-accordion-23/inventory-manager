package ledge.api.inventory.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductRequestDTO(
                UUID id,
                String name,
                BigDecimal purchasePrice,
                BigDecimal sellingPrice,
                int stockQuantity,
                BigDecimal taxRate) {
}
