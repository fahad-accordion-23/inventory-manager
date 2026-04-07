package ledge.application;

import ledge.domain.Product;
import ledge.domain.ProductService;
import ledge.application.dto.ProductDTO;
import ledge.application.event.*;
import ledge.util.event.Subscribe;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductController {
    private final ProductService productService;
    private final InventoryEventBroker eventBroker;

    public ProductController(ProductService productService, InventoryEventBroker eventBroker) {
        this.productService = productService;
        this.eventBroker = eventBroker;
        
        this.eventBroker.register(this);
    }

    @Subscribe
    private void handleProductAdded(ProductAddedEvent event) {
        productService.addProduct(fromDTO(event.getProduct()));
        eventBroker.publish(new ProductsUpdatedEvent(getAllProducts()));
    }

    @Subscribe
    private void handleProductRemoved(ProductRemovedEvent event) {
        productService.deleteProduct(event.getProductId());
        eventBroker.publish(new ProductsUpdatedEvent(getAllProducts()));
    }

    @Subscribe
    private void handleProductUpdated(ProductUpdatedEvent event) {
        productService.updateProduct(fromDTO(event.getProduct()));
        eventBroker.publish(new ProductsUpdatedEvent(getAllProducts()));
    }

    @Subscribe
    private void handleRefreshRequested(InventoryRefreshRequestedEvent event) {
        eventBroker.publish(new ProductsUpdatedEvent(getAllProducts()));
    }

    public Optional<ProductDTO> getProductById(UUID id) {
        return productService.getProductById(id).map(this::toDTO);
    }

    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(this::toDTO)
                .toList();
    }

    private ProductDTO toDTO(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPurchasePrice(),
            product.getSellingPrice(),
            product.getStockQuantity(),
            product.getTaxRate()
        );
    }

    private Product fromDTO(ProductDTO dto) {
        return new Product(
            dto.getId(),
            dto.getName(),
            dto.getPurchasePrice(),
            dto.getSellingPrice(),
            dto.getStockQuantity(),
            dto.getTaxRate()
        );
    }
}