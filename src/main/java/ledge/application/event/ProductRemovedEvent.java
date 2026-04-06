package ledge.application.event;

import java.util.Optional;
import java.util.UUID;
import ledge.util.event.Event;
import ledge.domain.Action;
import ledge.domain.Permission;
import ledge.domain.Resource;

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

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(new Permission(Resource.PRODUCT, Action.DELETE));
    }
}