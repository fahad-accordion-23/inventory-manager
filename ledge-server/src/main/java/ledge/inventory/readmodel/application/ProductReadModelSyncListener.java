package ledge.inventory.readmodel.application;

import ledge.inventory.events.domain.ProductCreatedDomainEvent;
import ledge.inventory.events.domain.ProductRemovedDomainEvent;
import ledge.inventory.events.domain.ProductUpdatedDomainEvent;
import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.inventory.readmodel.infrastructure.IProductReadRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener responsible for keeping the Inventory Read Model in sync with Domain changes.
 */
@Component
public class ProductReadModelSyncListener {
    private final IProductReadRepository readProductRepository;

    public ProductReadModelSyncListener(IProductReadRepository readProductRepository) {
        this.readProductRepository = readProductRepository;
    }

    @EventListener
    public void onProductCreated(ProductCreatedDomainEvent event) {
        readProductRepository.save(new ProductDTO(
                event.id(),
                event.name(),
                event.purchasePrice(),
                event.sellingPrice(),
                event.stockQuantity(),
                event.taxRate()
        ));
    }

    @EventListener
    public void onProductUpdated(ProductUpdatedDomainEvent event) {
        readProductRepository.save(new ProductDTO(
                event.id(),
                event.name(),
                event.purchasePrice(),
                event.sellingPrice(),
                event.stockQuantity(),
                event.taxRate()
        ));
    }

    @EventListener
    public void onProductRemoved(ProductRemovedDomainEvent event) {
        readProductRepository.delete(event.id());
    }
}
