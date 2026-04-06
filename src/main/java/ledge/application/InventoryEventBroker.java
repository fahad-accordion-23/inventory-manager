package ledge.application;

import ledge.security.AccessPolicy;
import ledge.util.event.Event;
import ledge.util.event.EventBroker;

/**
 * Concrete implementation of the event broker for the inventory manager.
 * Acts as the central hub for publishing and subscribing to application events.
 */
public class InventoryEventBroker extends EventBroker<Event> {
    
    public InventoryEventBroker() {
        super();
    }

    @Override
    public <E extends Event> void publish(E event) {
        event.getRequiredPermission().ifPresent(permission -> {
            AccessPolicy.require(permission.resource(), permission.action());
        });
        super.publish(event);
    }
}