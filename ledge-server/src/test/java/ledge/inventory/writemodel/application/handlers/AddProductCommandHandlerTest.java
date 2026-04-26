package ledge.inventory.writemodel.application.handlers;

import ledge.inventory.events.domain.ProductCreatedDomainEvent;
import ledge.inventory.writemodel.contracts.AddProductCommand;
import ledge.inventory.writemodel.domain.Product;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddProductCommandHandlerTest {

    private AddProductCommandHandler handler;
    private IProductWriteRepository writeRepository;
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        writeRepository = mock(IProductWriteRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        handler = new AddProductCommandHandler(writeRepository, eventPublisher);
    }

    @Test
    void testHandleAddProduct() {
        AddProductCommand command = new AddProductCommand(
                "Phone", new BigDecimal("500"), new BigDecimal("700"), 5, new BigDecimal("0.05"));

        handler.handle(command);

        // Verify write repository saved the product
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(writeRepository).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();
        assertEquals("Phone", savedProduct.getName());
        assertEquals(5, savedProduct.getStockQuantity());

        // Verify event was published for Read Model synchronization
        verify(eventPublisher).publishEvent(any(ProductCreatedDomainEvent.class));
    }
}
