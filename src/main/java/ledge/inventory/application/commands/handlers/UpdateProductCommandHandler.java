package ledge.inventory.application.commands.handlers;

import ledge.inventory.application.commands.UpdateProductCommand;
import ledge.inventory.application.events.ProductUpdatedEvent;
import ledge.inventory.application.services.ProductService;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.util.cqrs.CommandHandler;

public class UpdateProductCommandHandler {
    private final ProductService productService;
    private final InventoryEventBroker eventBroker;

    public UpdateProductCommandHandler(ProductService productService, InventoryEventBroker eventBroker) {
        this.productService = productService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handleProductUpdated(UpdateProductCommand command) {
        productService.updateProduct(command.getProduct().toProduct());
        eventBroker.publish(new ProductUpdatedEvent(command.getProduct().getId()));
    }
}
