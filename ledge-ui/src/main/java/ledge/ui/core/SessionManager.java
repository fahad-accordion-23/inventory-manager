package ledge.ui.core;

import java.util.Optional;

import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.ui.clients.HttpAuthClient;
import ledge.api.auth.dto.LoginRequestDTO;
import ledge.api.auth.dto.LoginResponseDTO;
import ledge.api.users.dto.response.UserResponseDTO;

/**
 * Client-side session manager for the UI.
 * Coordinates login/logout with the security service and preserves the current
 * auth token.
 */
public class SessionManager {
    private final HttpAuthClient authController;

    private AuthContext authContext;
    private UserResponseDTO currentUser;

    public SessionManager(HttpAuthClient authController) {
        this.authController = authController;
    }

    /**
     * Authenticates the user and stores the resulting token.
     * 
     * @param username The name to login with
     * @param password The password to login with
     * @throws Exception if login fails
     */
    public void login(String username, String password) throws Exception {
        LoginRequestDTO request = new LoginRequestDTO(username, password);
        ApiResponse<LoginResponseDTO> response = authController.login(request);

        if (response.success()) {
            this.authContext = new AuthContext(response.data().token());
            this.currentUser = response.data().user();
        } else {
            throw new Exception(response.error().message());
        }
    }

    /**
     * Clears the current session and invalidates the token on the server.
     */
    public void logout() {
        if (authContext != null) {
            authController.logout(authContext);
            authContext = null;
            currentUser = null;
        }
    }

    /**
     * Returns the current authentication token if the user is logged in.
     * 
     * @return Optional containing the token, or empty if not logged in.
     */
    public Optional<AuthContext> getAuthContext() {
        return Optional.ofNullable(authContext);
    }

    /**
     * Helper to check if a session is currently active.
     */
    public boolean isAuthenticated() {
        return authContext != null;
    }

    /**
     * Returns the user associated with the current session.
     */
    public Optional<UserResponseDTO> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    /**
     * Checks if the current user has the given capability.
     */
    public boolean isAllowed(Capability capability) {
        return currentUser.role().hasPermission(capability.permission());
    }
}
