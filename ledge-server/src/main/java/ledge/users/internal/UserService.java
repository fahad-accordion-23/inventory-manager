package ledge.users.internal;

import ledge.users.api.IUserService;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Internal implementation of the User OHS.
 * Delegates to the read repository.
 */
@Service
public class UserService implements IUserService {
    private final IUserReadRepository userReadRepository;

    public UserService(IUserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @Override
    public Optional<UserDTO> getUserById(UUID id) {
        return userReadRepository.findById(id);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        return userReadRepository.findByUsername(username);
    }
}
