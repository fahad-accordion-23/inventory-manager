package ledge.application;

import ledge.domain.Product;
import ledge.domain.ProductService;
import ledge.application.dto.ProductDTO;
import ledge.application.event.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductController {
    private final ProductService productService;
    private final InventoryEventBroker eventBroker;

    public ProductController(ProductService productService, InventoryEventBroker eventBroker) {
        this.productService = productService;
        this.eventBroker = eventBroker;
        
        this.eventBroker.subscribe(ProductAddedEvent.class, this::handleProductAdded);
        this.eventBroker.subscribe(ProductRemovedEvent.class, this::handleProductRemoved);
        this.eventBroker.subscribe(InventoryRefreshRequestedEvent.class, this::handleRefreshRequested);
    }

    private void handleProductAdded(ProductAddedEvent event) {
        ProductDTO dto = event.getProduct();
        
        Product domainProduct = new Product(
            dto.getId(),
            dto.getName(),
            dto.getPurchasePrice(),
            dto.getSellingPrice(),
            dto.getStockQuantity(),
            dto.getTaxRate()
        );
        
        productService.addProduct(domainProduct);
        eventBroker.publish(new ProductsUpdatedEvent(getAllProducts()));
    }

    private void handleProductRemoved(ProductRemovedEvent event) {
        productService.deleteProduct(event.getProductId());
        eventBroker.publish(new ProductsUpdatedEvent(getAllProducts()));
    }

    private void handleRefreshRequested(InventoryRefreshRequestedEvent event) {
        eventBroker.publish(new ProductsUpdatedEvent(getAllProducts()));
    }

    public Optional<ProductDTO> getProductById(UUID id) {
        return productService.getProductById(id).map(this::toDTO);
    }

    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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
}