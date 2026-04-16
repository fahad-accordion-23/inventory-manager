package ledge.util.cqrs;

import java.util.Optional;

import ledge.security.domain.Permission;

/**
 * Represents an intent to change the state of the system.
 */
public interface Command {
    default Optional<Permission> getRequiredPermission() {
        return Optional.empty();
    }
}
