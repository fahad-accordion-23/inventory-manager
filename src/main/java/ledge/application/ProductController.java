package ledge.application;

import ledge.domain.Product;
import ledge.domain.ProductService;
import ledge.application.dto.ProductDTO;
import ledge.application.command.AddProductCommand;
import ledge.application.command.RemoveProductCommand;
import ledge.application.command.UpdateProductCommand;
import ledge.application.query.GetAllProductsQuery;
import ledge.application.event.ProductAddedEvent;
import ledge.application.event.ProductRemovedEvent;
import ledge.application.event.ProductUpdatedEvent;
import ledge.application.event.ProductsUpdatedEvent;
import ledge.util.cqrs.CommandHandler;
import ledge.util.cqrs.QueryHandler;

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

    @CommandHandler
    private void handleAddProduct(AddProductCommand event) {
        productService.addProduct(fromDTO(event.getProduct()));
        eventBroker.publish(new ProductAddedEvent());
        eventBroker.publish(new ProductsUpdatedEvent());
    }

    @CommandHandler
    private void handleProductRemoved(RemoveProductCommand event) {
        productService.deleteProduct(event.getProductId());
        eventBroker.publish(new ProductRemovedEvent());
        eventBroker.publish(new ProductsUpdatedEvent());
    }

    @CommandHandler
    private void handleProductUpdated(UpdateProductCommand event) {
        productService.updateProduct(fromDTO(event.getProduct()));
        eventBroker.publish(new ProductUpdatedEvent(event.getProduct().getId()));
        eventBroker.publish(new ProductsUpdatedEvent());
    }

    @QueryHandler
    private List<ProductDTO> handleGetAllProducts(GetAllProductsQuery event) {
        return getAllProducts();
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
                product.getTaxRate());
    }

    private Product fromDTO(ProductDTO dto) {
        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getPurchasePrice(),
                dto.getSellingPrice(),
                dto.getStockQuantity(),
                dto.getTaxRate());
    }
}