package ledge.shared.infrastructure.commands;

import java.util.Optional;

import ledge.security.api.dto.PermissionDTO;

/**
 * Represents an intent to change the state of the system.
 */
public interface Command {
    default Optional<PermissionDTO> getRequiredPermission() {
        return Optional.empty();
    }
}
