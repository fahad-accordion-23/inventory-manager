package ledge.util.cqrs;

import ledge.domain.Permission;
import java.util.Optional;

/**
 * Represents an intent to change the state of the system.
 */
public interface Command {
    default Optional<Permission> getRequiredPermission() {
        return Optional.empty();
    }
}
