package ledge.security.domain;

import ledge.users.application.dtos.UserDTO;
import java.util.Optional;

public interface ISessionService {
    String createToken(UserDTO user);

    void removeToken(String token);

    Optional<UserDTO> getUserByToken(String token);
}
