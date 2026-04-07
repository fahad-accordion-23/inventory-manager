package ledge.util.event;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Brokers system events using the publish-subscribe pattern with weak references.
 * Automatically cleans up listeners when their parent objects are Garbage Collected.
 */
public abstract class EventBroker<T extends Event> {

    private static class SubscriberProxy {
        private final WeakReference<Object> subscriberRef;
        private final Method method;

        SubscriberProxy(Object subscriber, Method method) {
            this.subscriberRef = new WeakReference<>(subscriber);
            this.method = method;
            this.method.setAccessible(true);
        }

        /** Returns true if successfully invoked, false if the subscriber was GC'd. */
        boolean invoke(Event event) {
            Object subscriber = subscriberRef.get();
            if (subscriber == null) {
                return false;
            }
            try {
                method.invoke(subscriber, event);
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause();
                }
                throw new RuntimeException("Failed to invoke event handler", e);
            }
        }
    }

    private final Map<Class<? extends T>, List<SubscriberProxy>> registry = new ConcurrentHashMap<>();

    public void register(Object subscriber) {
        for (Method method : subscriber.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];
                if (Event.class.isAssignableFrom(paramType)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends T> eventType = (Class<? extends T>) paramType;
                    registry.computeIfAbsent(eventType, _k -> new CopyOnWriteArrayList<>())
                            .add(new SubscriberProxy(subscriber, method));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <E extends T> void publish(E event) {
        List<SubscriberProxy> proxies = registry.get(event.getClass());
        if (proxies != null) {
            // Evaluates invoke() for each subscriber and removes them if they return false (GC'd)
            proxies.removeIf(proxy -> !proxy.invoke(event));
        }
    }
}