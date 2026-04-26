package ledge.security.internal.domain.services;

import java.util.Optional;
import java.util.UUID;

public interface ISessionService {
    String createToken(UUID userId);

    void removeToken(String token);

    Optional<UUID> getUserIdByToken(String token);
}
