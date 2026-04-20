package ledge.ui;

import ledge.security.application.events.AuthenticationException;
import ledge.security.application.services.IAuthenticationService;
import ledge.users.application.dtos.UserDTO;
import ledge.users.application.query.GetUserByUsernameQuery;
import ledge.util.cqrs.QueryBus;

import java.util.Optional;

/**
 * Client-side session manager for the UI.
 * Coordinates login/logout with the security service and preserves the current
 * auth token.
 */
public class SessionManager {
    private final IAuthenticationService authService;
    private final QueryBus userQueryBus;

    private String authToken;
    private UserDTO currentUser;

    public SessionManager(IAuthenticationService authService, QueryBus userQueryBus) {
        this.authService = authService;
        this.userQueryBus = userQueryBus;
    }

    /**
     * Authenticates the user and stores the resulting token.
     * 
     * @param username The name to login with
     * @param password The password to login with
     * @throws AuthenticationException if login fails
     */
    public void login(String username, String password) throws AuthenticationException {
        this.authToken = authService.login(username, password);
        this.currentUser = userQueryBus.dispatch(new GetUserByUsernameQuery(username), authToken).get();
    }

    /**
     * Clears the current session and invalidates the token on the server.
     */
    public void logout() {
        if (authToken != null) {
            authService.logout(authToken);
            authToken = null;
        }
    }

    /**
     * Returns the current authentication token if the user is logged in.
     * 
     * @return Optional containing the token, or empty if not logged in.
     */
    public Optional<String> getAuthToken() {
        return Optional.ofNullable(authToken);
    }

    /**
     * Helper to check if a session is currently active.
     */
    public boolean isAuthenticated() {
        return authToken != null;
    }

    /**
     * Returns the user associated with the current session.
     */
    public Optional<UserDTO> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }
}
