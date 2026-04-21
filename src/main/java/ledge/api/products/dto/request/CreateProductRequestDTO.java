package ledge.api.products.dto.request;

import java.math.BigDecimal;

public record CreateProductRequestDTO(
        String name,
        BigDecimal purchasePrice,
        BigDecimal sellingPrice,
        int stockQuantity,
        BigDecimal taxRate) {}
