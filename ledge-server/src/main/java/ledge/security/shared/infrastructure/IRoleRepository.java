package ledge.security.shared.infrastructure;

import ledge.security.writemodel.domain.Role;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

/**
 * Interface for Role repository.
 */
public interface IRoleRepository {
    Optional<Role> findById(UUID id);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    void save(Role role);
}
