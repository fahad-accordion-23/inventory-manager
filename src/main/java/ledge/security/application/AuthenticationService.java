package ledge.security.application;

import ledge.security.application.events.AuthenticationException;
import ledge.security.domain.User;
import ledge.security.infrastructure.IUserRepository;
import ledge.util.PasswordHasher;

import java.util.Optional;

/**
 * Service for handling user authentication and token-based sessions.
 */
public class AuthenticationService {
    private final IUserRepository userRepository;
    private final SessionService sessionService;

    public AuthenticationService(
            IUserRepository userRepository,
            SessionService sessionService) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
    }

    /**
     * Authenticates a user and returns a session token if successful.
     * 
     * @param username The username to authenticate.
     * @param password The raw password to verify.
     * @return A unique session token string.
     * @throws AuthenticationException if authentication fails.
     */
    public String login(String username, String password) throws AuthenticationException {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new AuthenticationException("Invalid username or password");
        }

        User user = userOpt.get();
        if (!PasswordHasher.verify(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return sessionService.createToken(user);
    }

    /**
     * Terminates a session by removing the associated token.
     * 
     * @param token The session token to invalidate.
     */
    public void logout(String token) {
        if (token != null) {
            sessionService.removeToken(token);
        }
    }
}
