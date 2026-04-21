package ledge.shared.infrastructure.events;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Brokers system events using the publish-subscribe pattern with weak
 * references.
 * Automatically cleans up listeners when their parent objects are Garbage
 * Collected.
 */
public class EventBroker {

    private static class SubscriberProxy {
        private final WeakReference<Object> subscriberRef;
        private final Method method;

        SubscriberProxy(Object subscriber, Method method) {
            this.subscriberRef = new WeakReference<>(subscriber);
            this.method = method;
            this.method.setAccessible(true);
        }

        boolean invoke(Object event) {
            Object subscriber = subscriberRef.get();
            if (subscriber == null) {
                return false;
            }
            try {
                method.invoke(subscriber, event);
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                throw new RuntimeException("Failed to invoke event handler", e);
            }
        }
    }

    private final Map<Class<?>, List<SubscriberProxy>> registry = new ConcurrentHashMap<>();

    public void register(Object subscriber) {
        for (Method method : subscriber.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class))
                continue;
            if (method.getParameterCount() != 1)
                continue;

            Class<?> eventType = method.getParameterTypes()[0];

            registry
                    .computeIfAbsent(eventType, _k -> new CopyOnWriteArrayList<>())
                    .add(new SubscriberProxy(subscriber, method));
        }
    }

    public void publish(Object event) {
        List<SubscriberProxy> subscribers = registry.get(event.getClass());
        if (subscribers == null)
            return;

        subscribers.removeIf(proxy -> !proxy.invoke(event));
    }
}