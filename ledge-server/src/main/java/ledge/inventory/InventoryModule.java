package ledge.inventory;

import ledge.security.application.services.IAuthorizationService;
import ledge.boot.Module;
import ledge.boot.ModuleRegistry;
import ledge.inventory.readmodel.application.handlers.GetAllProductsQueryHandler;
import ledge.inventory.readmodel.infrastructure.IProductReadRepository;
import ledge.inventory.readmodel.infrastructure.ProductReadRepository;
import ledge.inventory.readmodel.infrastructure.messaging.InventoryQueryBus;
import ledge.inventory.writemodel.application.handlers.AddProductCommandHandler;
import ledge.inventory.writemodel.application.handlers.RemoveProductCommandHandler;
import ledge.inventory.writemodel.application.handlers.UpdateProductCommandHandler;
import ledge.inventory.writemodel.infrastructure.IProductWriteRepository;
import ledge.inventory.writemodel.infrastructure.ProductWriteRepository;
import ledge.inventory.writemodel.infrastructure.messaging.InventoryCommandBus;

public class InventoryModule implements Module {

        @Override
        public void register(ModuleRegistry registry) {

                registry.register(InventoryCommandBus.class, () -> {
                        InventoryCommandBus bus = new InventoryCommandBus(
                                        registry.resolve(IAuthorizationService.class));
                        bus.register(registry.resolve(AddProductCommandHandler.class));
                        bus.register(registry.resolve(RemoveProductCommandHandler.class));
                        bus.register(registry.resolve(UpdateProductCommandHandler.class));
                        return bus;
                });

                registry.register(InventoryQueryBus.class, () -> {
                        InventoryQueryBus bus = new InventoryQueryBus(
                                        registry.resolve(IAuthorizationService.class));
                        bus.register(registry.resolve(GetAllProductsQueryHandler.class));
                        return bus;
                });

                registry.register(IProductWriteRepository.class, () -> new ProductWriteRepository());
                registry.register(IProductReadRepository.class, () -> new ProductReadRepository());

                registry.register(AddProductCommandHandler.class, () -> new AddProductCommandHandler(
                                registry.resolve(IProductWriteRepository.class),
                                registry.resolve(IProductReadRepository.class)));

                registry.register(RemoveProductCommandHandler.class, () -> new RemoveProductCommandHandler(
                                registry.resolve(IProductWriteRepository.class),
                                registry.resolve(IProductReadRepository.class)));

                registry.register(UpdateProductCommandHandler.class, () -> new UpdateProductCommandHandler(
                                registry.resolve(IProductWriteRepository.class),
                                registry.resolve(IProductReadRepository.class)));

                registry.register(GetAllProductsQueryHandler.class, () -> new GetAllProductsQueryHandler(
                                registry.resolve(IProductReadRepository.class)));
        }

}
