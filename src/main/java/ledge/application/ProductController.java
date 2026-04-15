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
    private void handleAddProduct(AddProductCommand command) {
        productService.addProduct(fromDTO(command.getProduct()));
        eventBroker.publish(new ProductAddedEvent());
    }

    @CommandHandler
    private void handleProductRemoved(RemoveProductCommand command) {
        productService.deleteProduct(command.getProductId());
        eventBroker.publish(new ProductRemovedEvent());
    }

    @CommandHandler
    private void handleProductUpdated(UpdateProductCommand command) {
        productService.updateProduct(fromDTO(command.getProduct()));
        eventBroker.publish(new ProductUpdatedEvent(command.getProduct().getId()));
    }

    @QueryHandler
    private List<ProductDTO> handleGetAllProducts(GetAllProductsQuery query) {
        return productService.getAllProducts()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<ProductDTO> getProductById(UUID id) {
        return productService.getProductById(id).map(this::toDTO);
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