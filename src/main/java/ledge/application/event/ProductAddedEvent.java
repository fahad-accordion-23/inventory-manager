package ledge.application.event;

import ledge.util.event.Event;
import ledge.application.dto.ProductDTO;

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
}