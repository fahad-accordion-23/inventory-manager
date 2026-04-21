package ledge.users.writemodel.infrastructure;

import java.util.Optional;
import java.util.UUID;

import ledge.users.writemodel.domain.User;

public interface IUserWriteRepository {
    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID id);

    void save(User user);

    void delete(UUID id);
}
