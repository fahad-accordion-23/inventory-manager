package ledge.security.infrastructure;

import java.util.Optional;

import ledge.security.domain.User;

public interface IUserRepository {
    Optional<User> findByUsername(String username);

    void save(User user);
}
