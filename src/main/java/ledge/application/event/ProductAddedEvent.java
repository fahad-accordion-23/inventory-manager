package ledge.application.event;

import ledge.application.dto.ProductDTO;
import ledge.domain.Action;
import ledge.domain.Permission;
import ledge.domain.Resource;
import ledge.util.event.Event;
import java.util.Optional;

/**
 * Published when a new product is successfully added to the system.
 */
public class ProductAddedEvent implements Event {
    private final ProductDTO product;

    public ProductAddedEvent(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {
        return product;
    }

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(new Permission(Resource.PRODUCT, Action.CREATE));
    }
}