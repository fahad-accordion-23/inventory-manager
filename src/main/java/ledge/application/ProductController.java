package ledge.application;

import ledge.domain.Product;
import ledge.domain.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductController {
    private final ProductService productService;
    private final InventoryEventBroker eventBroker;

    public ProductController(ProductService productService, InventoryEventBroker eventBroker) {
        this.productService = productService;
        this.eventBroker = eventBroker;
        
        this.eventBroker.subscribe(ProductAddedEvent.class, this::handleProductAdded);
        this.eventBroker.subscribe(ProductRemovedEvent.class, this::handleProductRemoved);
    }

    private void handleProductAdded(ProductAddedEvent event) {
        productService.addProduct(event.getProduct());
        eventBroker.publish(new ProductsUpdatedEvent(productService.getAllProducts()));
    }

    private void handleProductRemoved(ProductRemovedEvent event) {
        productService.deleteProduct(event.getProductId());
        eventBroker.publish(new ProductsUpdatedEvent(productService.getAllProducts()));
    }

    public Optional<Product> getProductById(UUID id) {
        return productService.getProductById(id);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}