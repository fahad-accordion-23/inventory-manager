package ledge.inventory.application.commands.handlers;

import ledge.inventory.application.commands.AddProductCommand;
import ledge.inventory.application.events.ProductAddedEvent;
import ledge.inventory.application.services.IProductService;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.util.cqrs.CommandHandler;

public class AddProductCommandHandler {
    private final IProductService productService;
    private final InventoryEventBroker eventBroker;

    public AddProductCommandHandler(IProductService productService, InventoryEventBroker eventBroker) {
        this.productService = productService;
        this.eventBroker = eventBroker;
    }

    @CommandHandler
    public void handleAddProduct(AddProductCommand command) {
        productService.addProduct(command.product().toProduct());
        eventBroker.publish(new ProductAddedEvent());
    }
}
