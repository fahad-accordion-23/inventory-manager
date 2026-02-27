package ledge.application;

import ledge.application.event.Event;
import ledge.domain.Product;

/**
 * Published when a new product is successfully added to the system.
 */
public class ProductAddedEvent implements Event {
    private final Product product;

    public ProductAddedEvent(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}