package ledge.util.event;

/**
 * Functional interface for receiving and handling system events.
 * * @param <E> The specific type of event to handle.
 */
@FunctionalInterface
public interface EventListener<E extends Event> {
    void onEvent(E event);
}