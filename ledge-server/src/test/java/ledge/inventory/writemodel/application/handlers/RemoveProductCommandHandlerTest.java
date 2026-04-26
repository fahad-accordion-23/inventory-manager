package ledge.inventory.writemodel.application.handlers;

import ledge.inventory.events.domain.ProductRemovedDomainEvent;
import ledge.inventory.writemodel.contracts.RemoveProductCommand;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static org.mockito.Mockito.*;

class RemoveProductCommandHandlerTest {

    private RemoveProductCommandHandler handler;
    private IProductWriteRepository writeRepository;
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        writeRepository = mock(IProductWriteRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        handler = new RemoveProductCommandHandler(writeRepository, eventPublisher);
    }

    @Test
    void testHandleRemoveProduct() {
        UUID productId = UUID.randomUUID();
        RemoveProductCommand command = new RemoveProductCommand(productId);
        
        handler.handle(command);

        // Verify write repository had the product deleted
        verify(writeRepository).delete(productId);

        // Verify event was published for Read Model synchronization
        verify(eventPublisher).publishEvent(any(ProductRemovedDomainEvent.class));
    }
}
