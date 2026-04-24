package ledge.shared.infrastructure.queries;

import java.util.Optional;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;

/**
 * Represents a request to read data from the system without causing side
 * effects.
 * 
 * @param <R> The type of the result returned by this query.
 */
public interface Query<R> {
    default Optional<PermissionDTO> getRequiredPermission() {
        return Optional.empty();
    }
}
