package ledge.application.event;

import java.util.UUID;

import ledge.util.event.Event;

/**
 * Published when an existing product's details are updated.
 */
public class ProductUpdatedEvent implements Event {

    private final UUID productId;

    public ProductUpdatedEvent(UUID productId) {
        this.productId = productId;
    }

    public UUID getProductId() {
        return productId;
    }
}
