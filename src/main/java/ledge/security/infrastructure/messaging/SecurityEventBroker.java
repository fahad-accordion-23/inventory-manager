package ledge.security.infrastructure.messaging;

import ledge.util.event.Event;
import ledge.util.event.EventBroker;

/**
 * Concrete implementation of the event broker for the security manager.
 * Acts as the central hub for publishing and subscribing to security
 * application events.
 */
public class SecurityEventBroker extends EventBroker<Event> {

}
