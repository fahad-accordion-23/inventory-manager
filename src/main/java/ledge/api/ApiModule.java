package ledge.api;

import ledge.api.auth.AuthController;
import ledge.api.inventory.InventoryController;
import ledge.api.users.UserController;
import ledge.boot.Module;
import ledge.boot.ModuleRegistry;
import ledge.inventory.readmodel.infrastructure.messaging.InventoryQueryBus;
import ledge.inventory.writemodel.infrastructure.messaging.InventoryCommandBus;
import ledge.security.application.services.IAuthenticationService;
import ledge.security.domain.ISessionService;
import ledge.users.readmodel.infrastructure.messaging.UserQueryBus;
import ledge.users.writemodel.infrastructure.messaging.UserCommandBus;

public class ApiModule implements Module {

        @Override
        public void register(ModuleRegistry registry) {
                registry.register(AuthController.class, () -> new AuthController(
                                registry.resolve(IAuthenticationService.class),
                                registry.resolve(ISessionService.class)));

                registry.register(InventoryController.class, () -> new InventoryController(
                                registry.resolve(InventoryCommandBus.class),
                                registry.resolve(InventoryQueryBus.class)));

                registry.register(UserController.class, () -> new UserController(
                                registry.resolve(UserCommandBus.class),
                                registry.resolve(UserQueryBus.class)));
        }

}
