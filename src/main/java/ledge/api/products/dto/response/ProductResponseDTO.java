package ledge.api.products.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * response DTO for product data.
 */
public record ProductResponseDTO(
        UUID id,
        String name,
        BigDecimal purchasePrice,
        BigDecimal sellingPrice,
        int stockQuantity,
        BigDecimal taxRate) {}
