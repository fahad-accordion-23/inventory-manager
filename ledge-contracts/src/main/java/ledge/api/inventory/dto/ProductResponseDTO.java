package ledge.api.inventory.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDTO(
                UUID id,
                String name,
                BigDecimal purchasePrice,
                BigDecimal sellingPrice,
                int stockQuantity,
                BigDecimal taxRate) {
}
