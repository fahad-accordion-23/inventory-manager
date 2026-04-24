package ledge.shared.infrastructure.commands;

import java.util.Optional;

import ledge.shared.types.Permission;

/**
 * Represents an intent to change the state of the system.
 */
public interface Command {
    default Optional<Permission> getRequiredPermission() {
        return Optional.empty();
    }
}
