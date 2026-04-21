package ledge.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModuleRegistryTest {

    private ModuleRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ModuleRegistry();
    }

    @Test
    void testSingletonResolution() {
        registry.register(String.class, () -> new String("singleton"));
        
        String instance1 = registry.resolve(String.class);
        String instance2 = registry.resolve(String.class);
        
        assertSame(instance1, instance2);
        assertEquals("singleton", instance1);
    }

    @Test
    void testResolveUnregisteredService() {
        assertThrows(IllegalStateException.class, () -> registry.resolve(Integer.class));
    }

    @Test
    void testDependencyChain() {
        registry.register(ServiceA.class, () -> new ServiceA());
        registry.register(ServiceB.class, () -> new ServiceB(registry.resolve(ServiceA.class)));
        
        ServiceB b = registry.resolve(ServiceB.class);
        assertNotNull(b);
        assertNotNull(b.a);
    }

    static class ServiceA {}
    static class ServiceB {
        final ServiceA a;
        ServiceB(ServiceA a) { this.a = a; }
    }
}
