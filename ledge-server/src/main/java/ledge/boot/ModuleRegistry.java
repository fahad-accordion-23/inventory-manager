package ledge.boot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModuleRegistry {

    private final Map<Class<?>, Supplier<?>> providers = new HashMap<>();
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public <T> void register(Class<T> type, Supplier<T> provider) {
        providers.put(type, provider);
    }

    public <T> T resolve(Class<T> type) {

        if (instances.containsKey(type)) {
            return type.cast(instances.get(type));
        }

        if (!providers.containsKey(type)) {
            throw new IllegalStateException("Service not registered: " + type);
        }

        Supplier<?> provider = providers.get(type);

        if (provider == null) {
            throw new RuntimeException("No provider registered for " + type);
        }

        T instance = type.cast(provider.get());
        instances.put(type, instance);

        return instance;
    }
}