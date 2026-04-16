package ledge.infrastructure;

import java.util.Optional;

import ledge.security.domain.User;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    void save(User user);
}
