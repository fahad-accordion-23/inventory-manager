package ledge.inventory.writemodel.application.handlers;

import ledge.inventory.events.domain.ProductRemovedDomainEvent;
import ledge.inventory.writemodel.contracts.RemoveProductCommand;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import ledge.shared.infrastructure.commands.CommandHandler;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Handler for removing a product from inventory.
 * Decoupled from the Read Model via Spring Events.
 */
@Service
public class RemoveProductCommandHandler implements CommandHandler<RemoveProductCommand> {
    private final IProductWriteRepository writeProductRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RemoveProductCommandHandler(IProductWriteRepository writeProductRepository,
                                      ApplicationEventPublisher eventPublisher) {
        this.writeProductRepository = writeProductRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(RemoveProductCommand command) {
        writeProductRepository.delete(command.productId());

        // Publish event for Read Model synchronization
        eventPublisher.publishEvent(new ProductRemovedDomainEvent(command.productId()));
    }
}
