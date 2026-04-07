package ledge.util.event;

import ledge.domain.Permission;
import java.util.Optional;

/**
 * Represents a system event within the application layer.
 */
public interface Event {
    default Optional<Permission> getRequiredPermission() {
        return Optional.empty();
    }
}