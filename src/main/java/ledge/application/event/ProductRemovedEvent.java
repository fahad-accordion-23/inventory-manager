package ledge.application.event;

import java.util.UUID;
import ledge.util.event.Event;

/**
 * Published when an existing product is removed from the system.
 */
public class ProductRemovedEvent implements Event {
    private final UUID productId;

    public ProductRemovedEvent(UUID productId) {
        this.productId = productId;
    }

    public UUID getProductId() {
        return productId;
    }
}