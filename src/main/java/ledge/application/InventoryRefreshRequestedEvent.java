package ledge.application;

import ledge.application.event.Event;

/**
 * Published by the UI when it needs the latest baseline of data (e.g., on dashboard load).
 */
public class InventoryRefreshRequestedEvent implements Event {
    public InventoryRefreshRequestedEvent() {
    }
}
