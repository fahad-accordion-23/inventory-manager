package ledge.inventory.writemodel.application.handlers;

import ledge.inventory.events.domain.ProductCreatedDomainEvent;
import ledge.inventory.writemodel.contracts.AddProductCommand;
import ledge.inventory.writemodel.domain.Product;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import ledge.shared.infrastructure.commands.CommandHandler;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Handler for adding a new product.
 * Decoupled from the Read Model via Spring Events.
 */
@Service
public class AddProductCommandHandler implements CommandHandler<AddProductCommand> {
    private final IProductWriteRepository writeProductRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AddProductCommandHandler(IProductWriteRepository writeProductRepository,
                                   ApplicationEventPublisher eventPublisher) {
        this.writeProductRepository = writeProductRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(AddProductCommand command) {
        Product product = Product.register(
                command.name(),
                command.purchasePrice(),
                command.sellingPrice(),
                command.stockQuantity(),
                command.taxRate());

        writeProductRepository.save(product);

        // Publish event for Read Model synchronization
        eventPublisher.publishEvent(new ProductCreatedDomainEvent(
                product.getId(),
                product.getName(),
                product.getPurchasePrice(),
                product.getSellingPrice(),
                product.getStockQuantity(),
                product.getTaxRate()
        ));
    }
}
