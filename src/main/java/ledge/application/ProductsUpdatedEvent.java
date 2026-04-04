package ledge.application;

import ledge.application.event.Event;
import ledge.domain.Product;
import java.util.List;

/**
 * Published whenever the list of all products is updated (e.g., added, removed).
 */
public class ProductsUpdatedEvent implements Event {
    private final List<Product> products;

    public ProductsUpdatedEvent(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
}
