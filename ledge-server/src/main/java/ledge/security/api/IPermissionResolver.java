package ledge.security.api;

import ledge.security.api.dto.PermissionDTO;
import java.util.Optional;

/**
 * Strategy for resolving permissions from a request context (e.g., Command or Query).
 */
public interface IPermissionResolver {
    /**
     * Resolves the required permission for the given context object.
     * 
     * @param context The object to resolve (e.g., an annotated command).
     * @return An Optional containing the PermissionDTO if found, or empty.
     */
    Optional<PermissionDTO> resolve(Object context);
}
