package ledge.security.application.services;

import ledge.security.application.events.AuthenticationException;
import ledge.security.domain.SessionService;
import ledge.users.application.dtos.UserDTO;
import ledge.users.application.services.IUserService;
import ledge.users.domain.User;
import ledge.util.PasswordHasher;

import java.util.Optional;

/**
 * Service for handling user authentication and token-based sessions.
 */
public class AuthenticationService {
    private final IUserService userService;
    private final SessionService sessionService;

    public AuthenticationService(
            IUserService userService,
            SessionService sessionService) {
        this.userService = userService;
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
        Optional<User> userOpt = userService.getUserByUsername(username);

        if (userOpt.isEmpty()) {
            throw new AuthenticationException("Invalid username or password");
        }

        User user = userOpt.get();

        if (!PasswordHasher.verify(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return sessionService.createToken(UserDTO.fromUser(user));
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
