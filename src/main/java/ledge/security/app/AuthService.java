package ledge.security.app;

import ledge.infrastructure.UserRepository;
import ledge.security.app.event.AuthenticationException;
import ledge.security.domain.User;
import ledge.util.PasswordHasher;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) throws AuthenticationException {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new AuthenticationException("Invalid username or password");
        }

        User user = userOpt.get();
        if (!PasswordHasher.verify(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password");
        }

        SecurityContext.setCurrentUser(user);
        return user;
    }

    public void logout() {
        SecurityContext.clear();
    }
}
