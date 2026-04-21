package ledge.users.readmodel.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ledge.users.readmodel.dtos.UserDTO;

public interface IUserReadRepository {

    Optional<UserDTO> findById(UUID id);

    Optional<UserDTO> findByUsername(String username);

    List<UserDTO> findAll();

    void save(UserDTO user);

    void deleteById(UUID id);

}