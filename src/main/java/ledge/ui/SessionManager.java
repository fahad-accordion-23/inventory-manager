package ledge.ui;

import ledge.security.application.AuthenticationService;
import ledge.security.application.SessionService;
import ledge.security.application.events.AuthenticationException;
import ledge.security.domain.User;

import java.util.Optional;

/**
 * Client-side session manager for the UI.
 * Coordinates login/logout with the security service and preserves the current
 * auth token.
 */
public class SessionManager {
    private final AuthenticationService authService;
    private final SessionService sessionService;
    private String authToken;

    public SessionManager(AuthenticationService authService, SessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
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
    public Optional<User> getCurrentUser() {
        return getAuthToken().flatMap(sessionService::getUserByToken);
    }
}
