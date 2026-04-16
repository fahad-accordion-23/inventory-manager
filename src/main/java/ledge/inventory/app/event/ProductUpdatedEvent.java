package ledge.inventory.app.event;

import java.util.UUID;

/**
 * Published when an existing product's details are updated.
 */
public class ProductUpdatedEvent extends ProductsUpdatedEvent {

    private final UUID productId;

    public ProductUpdatedEvent(UUID productId) {
        this.productId = productId;
    }

    public UUID getProductId() {
        return productId;
    }
}
