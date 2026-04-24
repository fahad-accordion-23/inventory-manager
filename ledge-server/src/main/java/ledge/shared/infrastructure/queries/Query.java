package ledge.shared.infrastructure.queries;

import java.util.Optional;

import ledge.security.writemodel.domain.Permission;

/**
 * Represents a request to read data from the system without causing side
 * effects.
 * 
 * @param <R> The type of the result returned by this query.
 */
public interface Query<R> {
    default Optional<Permission> getRequiredPermission() {
        return Optional.empty();
    }
}
