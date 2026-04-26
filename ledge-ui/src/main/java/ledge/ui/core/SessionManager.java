package ledge.ui.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.ui.clients.HttpAuthClient;
import ledge.api.auth.dto.request.LoginRequestDTO;
import ledge.api.auth.dto.response.LoginResponseDTO;
import ledge.api.users.dto.UserResponseDTO;

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
     * Refreshes the current user info using the auth token.
     */
    public void refreshSession() {
        if (authContext == null)
            return;

        ApiResponse<UserResponseDTO> response = authController.me(authContext);
        if (response.success()) {
            this.currentUser = response.data();
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
     * Updated to check against the map-based permission structure containing multiple actions per resource.
     */
    public boolean isAllowed(Capability capability) {
        if (currentUser == null || currentUser.role() == null) {
            return false;
        }

        Action actionToDo = capability.action();
        Resource resourceNeeded = capability.resource();

        Map<Resource, List<Action>> permissions = currentUser.role().permissions();

        List<Action> allowedActions = permissions.get(resourceNeeded);

        return allowedActions != null && allowedActions.contains(actionToDo);
    }
}
