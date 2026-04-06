package ledge.application.event;

import ledge.domain.Action;
import ledge.domain.Permission;
import ledge.domain.Resource;
import ledge.util.event.Event;
import java.util.Optional;

/**
 * Published by the UI when it needs the latest baseline of data (e.g., on dashboard load).
 */
public class InventoryRefreshRequestedEvent implements Event {
    public InventoryRefreshRequestedEvent() {
    }

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(new Permission(Resource.PRODUCT, Action.READ));
    }
}
