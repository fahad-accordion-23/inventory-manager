package ledge.api.auth;

import ledge.api.auth.dto.LoginRequestDTO;
import ledge.api.auth.dto.LoginResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.security.application.events.AuthenticationException;
import ledge.security.application.services.IAuthenticationService;
import ledge.security.domain.ISessionService;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.Optional;

/**
 * Controller for handling authentication-related API requests.
 */
public class AuthController {
    private final IAuthenticationService authService;
    private final ISessionService sessionService;

    public AuthController(IAuthenticationService authService, ISessionService sessionService) {
        this.authService = authService;
        this.sessionService = sessionService;
    }

    /**
     * Authenticates a user and returns a token and user details.
     */
    public ApiResponse<LoginResponseDTO> login(LoginRequestDTO request) {
        try {
            String token = authService.login(request.username(), request.password());
            Optional<UserDTO> userOpt = sessionService.getUserByToken(token);

            if (userOpt.isEmpty()) {
                return ApiResponse.error("Failed to resolve user session", "SESSION_ERROR");
            }

            UserDTO user = userOpt.get();
            UserResponseDTO userResponse = new UserResponseDTO(user.id(), user.username(), user.role());
            return ApiResponse.success(new LoginResponseDTO(token, userResponse));
        } catch (AuthenticationException e) {
            return ApiResponse.error(e.getMessage(), "AUTH_FAILED");
        }
    }

    /**
     * Invalidates the provided authentication token.
     */
    public ApiResponse<Void> logout(AuthContext context) {
        if (context != null && context.token() != null) {
            authService.logout(context.token());
        }
        return ApiResponse.success(null);
    }
}
