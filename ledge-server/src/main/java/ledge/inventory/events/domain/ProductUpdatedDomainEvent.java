package ledge.inventory.events.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Domain event fired when product details are updated.
 */
public record ProductUpdatedDomainEvent(
    UUID id,
    String name,
    BigDecimal purchasePrice,
    BigDecimal sellingPrice,
    int stockQuantity,
    BigDecimal taxRate
) {}
