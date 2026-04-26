package ledge.inventory.events.domain;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Domain event fired when a new product is added to the inventory.
 */
public record ProductCreatedDomainEvent(
    UUID id,
    String name,
    BigDecimal purchasePrice,
    BigDecimal sellingPrice,
    int stockQuantity,
    BigDecimal taxRate
) {}
