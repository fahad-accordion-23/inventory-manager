package ledge.users.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ledge.users.domain.User;

public interface IUserRepository {
    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID id);

    List<User> findAll();

    void save(User user);

    void update(User user);

    void delete(UUID id);
}
