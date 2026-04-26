package ledge.security.internal.application;

import ledge.security.api.exceptions.AuthenticationException;
import ledge.security.api.IAuthenticationService;
import ledge.security.internal.domain.services.ISessionService;
import ledge.users.api.IUserService;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.util.PasswordHasher;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * Service for handling user authentication and token-based sessions.
 * Now aligned with the Users Module Open Host Service (OHS).
 */
@Service
public class AuthenticationService implements IAuthenticationService {
    private final ISessionService sessionService;
    private final IUserService userService;

    public AuthenticationService(ISessionService sessionService, IUserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    /**
     * Authenticates a user and returns a session token if successful.
     * 
     * @param username The username to authenticate.
     * @param password The raw password to verify.
     * @return A unique session token string.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public String login(String username, String password) throws AuthenticationException {
        Optional<UserDTO> userOpt = userService.getUserByUsername(username);

        if (userOpt.isEmpty()) {
            throw new AuthenticationException("Invalid username or password");
        }

        UserDTO user = userOpt.get();

        if (!PasswordHasher.verify(password, user.hashedPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        return sessionService.createToken(user.id());
    }

    /**
     * Terminates a session by removing the associated token.
     * 
     * @param token The session token to invalidate.
     */
    @Override
    public void logout(String token) {
        if (token != null) {
            sessionService.removeToken(token);
        }
    }

    @Override
    public Optional<UUID> getUserIdByToken(String token) {
        return sessionService.getUserIdByToken(token);
    }
}
