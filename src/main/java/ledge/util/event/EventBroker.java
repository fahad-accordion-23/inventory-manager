package ledge.util.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Brokers system events using the publish-subscribe pattern.
 * Facilitates decoupled communication across the application layer.
 */
public abstract class EventBroker<T extends Event> {
    
    private final Map<Class<? extends T>, List<EventListener<? extends T>>> listeners = new HashMap<>();
    
    public <E extends T> void subscribe(Class<E> eventType, EventListener<E> listener) {
        listeners.computeIfAbsent(eventType, _ -> new ArrayList<>()).add(listener);
    }
    
    @SuppressWarnings("unchecked")
    public <E extends T> void publish(E event) {
        List<EventListener<? extends T>> list = listeners.get(event.getClass());
        
        if (list != null) {
            for (EventListener<? extends T> listener : list) {
                ((EventListener<E>) listener).onEvent(event);
            }
        }
    }
}