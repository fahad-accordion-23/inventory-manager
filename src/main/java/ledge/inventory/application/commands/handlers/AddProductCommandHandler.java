package ledge.inventory.application.commands.handlers;

import ledge.inventory.application.commands.AddProductCommand;
import ledge.inventory.application.events.ProductAddedEvent;
import ledge.inventory.application.services.ProductService;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.util.cqrs.CommandHandler;

public class AddProductCommandHandler {
    private final ProductService productService;
    private final InventoryEventBroker eventBroker;

    public AddProductCommandHandler(ProductService productService, InventoryEventBroker eventBroker) {
        this.productService = productService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handleAddProduct(AddProductCommand command) {
        productService.addProduct(command.getProduct().toProduct());
        eventBroker.publish(new ProductAddedEvent());
    }
}
