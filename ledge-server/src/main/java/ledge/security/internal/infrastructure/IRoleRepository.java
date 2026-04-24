package ledge.security.internal.infrastructure;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import ledge.security.internal.domain.models.Role;

/**
 * Interface for Role repository.
 */
public interface IRoleRepository {
    Optional<Role> findById(UUID id);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    void save(Role role);
}
