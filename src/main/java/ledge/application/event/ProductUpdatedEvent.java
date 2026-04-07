package ledge.application.event;

import ledge.application.dto.ProductDTO;
import ledge.domain.Action;
import ledge.domain.Permission;
import ledge.domain.Resource;
import ledge.util.event.Event;
import java.util.Optional;

/**
 * Published when an existing product's details are updated.
 */
public class ProductUpdatedEvent implements Event {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.UPDATE);

    private final ProductDTO product;

    public ProductUpdatedEvent(ProductDTO product) {
        this.product = product;
    }

    public ProductDTO getProduct() {
        return product;
    }

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
