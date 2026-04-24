package ledge.boot;

import java.util.List;

import ledge.api.ApiModule;
import ledge.inventory.InventoryModule;
import ledge.security.SecurityModule;
import ledge.users.UsersModule;

public class Modules {

    public static ModuleRegistry bootstrap() {

        ModuleRegistry registry = new ModuleRegistry();

        List<Module> modules = List.of(
                new SecurityModule(),
                new UsersModule(),
                new InventoryModule(),
                new ApiModule());

        for (Module module : modules) {
            module.register(registry);
        }

        return registry;
    }

}