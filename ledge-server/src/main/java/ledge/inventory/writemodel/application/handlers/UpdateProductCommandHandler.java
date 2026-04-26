package ledge.inventory.writemodel.application.handlers;

import ledge.inventory.events.domain.ProductUpdatedDomainEvent;
import ledge.inventory.writemodel.contracts.UpdateProductCommand;
import ledge.inventory.writemodel.domain.Product;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import ledge.shared.infrastructure.commands.CommandHandler;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Handler for updating an existing product.
 * Decoupled from the Read Model via Spring Events.
 */
@Service
public class UpdateProductCommandHandler implements CommandHandler<UpdateProductCommand> {
    private final IProductWriteRepository writeProductRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UpdateProductCommandHandler(IProductWriteRepository writeProductRepository,
                                      ApplicationEventPublisher eventPublisher) {
        this.writeProductRepository = writeProductRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(UpdateProductCommand command) {
        Product updatedProduct = Product.rehydrate(
                command.id(),
                command.name(),
                command.purchasePrice(),
                command.sellingPrice(),
                command.stockQuantity(),
                command.taxRate());

        writeProductRepository.save(updatedProduct);

        // Publish event for Read Model synchronization
        eventPublisher.publishEvent(new ProductUpdatedDomainEvent(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getPurchasePrice(),
                updatedProduct.getSellingPrice(),
                updatedProduct.getStockQuantity(),
                updatedProduct.getTaxRate()
        ));
    }
}
