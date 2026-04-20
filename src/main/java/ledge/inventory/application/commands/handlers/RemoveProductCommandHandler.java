package ledge.inventory.application.commands.handlers;

import ledge.inventory.application.commands.RemoveProductCommand;
import ledge.inventory.application.events.ProductRemovedEvent;
import ledge.inventory.application.services.ProductService;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.util.cqrs.CommandHandler;

public class RemoveProductCommandHandler {
    private final ProductService productService;
    private final InventoryEventBroker eventBroker;

    public RemoveProductCommandHandler(ProductService productService, InventoryEventBroker eventBroker) {
        this.productService = productService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handleProductRemoved(RemoveProductCommand command) {
        productService.deleteProduct(command.getProductId());
        eventBroker.publish(new ProductRemovedEvent());
    }
}
