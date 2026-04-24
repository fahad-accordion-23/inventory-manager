package ledge.boot;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import ledge.api.auth.AuthController;
import ledge.api.inventory.InventoryController;
import ledge.api.users.UserController;

@Configuration
public class ServerConfig {
    
    @Bean
    public ModuleRegistry moduleRegistry() {
        return Modules.bootstrap();
    }

    @Bean
    public AuthController authController(ModuleRegistry registry) {
        return registry.resolve(AuthController.class);
    }

    @Bean
    public InventoryController inventoryController(ModuleRegistry registry) {
        return registry.resolve(InventoryController.class);
    }

    @Bean
    public UserController userController(ModuleRegistry registry) {
        return registry.resolve(UserController.class);
    }

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(new Gson());
        return converter;
    }
}
