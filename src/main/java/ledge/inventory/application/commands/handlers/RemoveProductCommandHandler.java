package ledge.inventory.application.commands.handlers;

import ledge.inventory.application.commands.RemoveProductCommand;
import ledge.inventory.application.events.ProductRemovedEvent;
import ledge.inventory.application.services.IProductService;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.util.cqrs.CommandHandler;

public class RemoveProductCommandHandler {
    private final IProductService productService;
    private final InventoryEventBroker eventBroker;

    public RemoveProductCommandHandler(IProductService productService, InventoryEventBroker eventBroker) {
        this.productService = productService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handleProductRemoved(RemoveProductCommand command) {
        productService.deleteProduct(command.productId());
        eventBroker.publish(new ProductRemovedEvent());
    }
}
