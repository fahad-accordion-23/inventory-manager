package ledge.application;

import ledge.application.event.Event;
import ledge.application.event.EventBroker;

/**
 * Concrete implementation of the event broker for the inventory manager.
 * Acts as the central hub for publishing and subscribing to application events.
 */
public class InventoryEventBroker extends EventBroker<Event> {
    
    public InventoryEventBroker() {
        super();
    }
}