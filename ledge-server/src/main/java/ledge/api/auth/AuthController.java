package ledge.api.auth;

import ledge.api.auth.dto.LoginRequestDTO;
import ledge.api.auth.dto.LoginResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.security.writemodel.application.events.AuthenticationException;
import ledge.security.writemodel.application.services.IAuthenticationService;
import ledge.security.writemodel.domain.services.ISessionService;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.Optional;

import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related API requests.
 */
@RestController
@RequestMapping("/api/auth")
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
    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
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
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
        }
        return ApiResponse.success(null);
    }
}
