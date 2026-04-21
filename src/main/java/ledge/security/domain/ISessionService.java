package ledge.security.domain;

import java.util.Optional;

import ledge.users.readmodel.dtos.UserDTO;

public interface ISessionService {
    String createToken(UserDTO user);

    void removeToken(String token);

    Optional<UserDTO> getUserByToken(String token);
}
