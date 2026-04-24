package ledge.security.shared.infrastructure;

import ledge.security.writemodel.domain.Role;
import java.util.Optional;
import java.util.List;

/**
 * Interface for Role repository.
 */
public interface IRoleRepository {
    Optional<Role> findByName(String name);

    List<Role> findAll();

    void save(Role role);
}
