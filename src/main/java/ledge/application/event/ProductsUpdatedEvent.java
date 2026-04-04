package ledge.application.event;

import ledge.util.event.Event;

import ledge.application.dto.ProductDTO;
import java.util.List;

/**
 * Published whenever the list of all products is updated (e.g., added, removed).
 */
public class ProductsUpdatedEvent implements Event {
    private final List<ProductDTO> products;

    public ProductsUpdatedEvent(List<ProductDTO> products) {
        this.products = products;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }
}
