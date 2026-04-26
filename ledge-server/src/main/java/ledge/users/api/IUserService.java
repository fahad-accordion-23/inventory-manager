package ledge.users.api;

import ledge.users.readmodel.dtos.UserDTO;
import java.util.Optional;
import java.util.UUID;

/**
 * Open Host Service (OHS) for the Users bounded context.
 * Exposes safe, formal methods for other modules to interact with users.
 */
public interface IUserService {

    /**
     * Retrieves a user by their unique identity.
     */
    Optional<UserDTO> getUserById(UUID id);

    /**
     * Retrieves a user by their unique username.
     * Useful for authentication flows.
     */
    Optional<UserDTO> getUserByUsername(String username);

}
