package ledge.inventory.writemodel.application.handlers;

import ledge.inventory.readmodel.infrastructure.IProductReadRepository;
import ledge.inventory.writemodel.contracts.RemoveProductCommand;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

class RemoveProductCommandHandlerTest {

    private RemoveProductCommandHandler handler;
    private IProductWriteRepository writeRepository;
    private IProductReadRepository readRepository;

    @BeforeEach
    void setUp() {
        writeRepository = mock(IProductWriteRepository.class);
        readRepository = mock(IProductReadRepository.class);
        handler = new RemoveProductCommandHandler(writeRepository, readRepository);
    }

    @Test
    void testHandleRemoveProduct() {
        UUID productId = UUID.randomUUID();
        RemoveProductCommand command = new RemoveProductCommand(productId);
        
        handler.handleProductRemoved(command);

        // Verify both repositories had the product deleted
        verify(writeRepository).delete(productId);
        verify(readRepository).delete(productId);
    }
}
