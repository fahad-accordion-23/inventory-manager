package ledge.api.auth;

import ledge.api.auth.dto.LoginRequestDTO;
import ledge.api.auth.dto.LoginResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.security.api.dto.RoleDTO;
import ledge.security.api.exceptions.AuthenticationException;
import ledge.security.internal.domain.services.ISessionService;
import ledge.security.api.IAuthenticationService;
import ledge.security.api.IUserRoleService;
import ledge.users.readmodel.contracts.GetUserByIdQuery;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.shared.infrastructure.queries.QueryBus;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related API requests.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthenticationService authService;
    private final ISessionService sessionService;
    private final IUserRoleService userRoleService;
    private final QueryBus queryBus;

    public AuthController(IAuthenticationService authService, ISessionService sessionService,
            IUserRoleService userRoleService, QueryBus queryBus) {
        this.authService = authService;
        this.sessionService = sessionService;
        this.userRoleService = userRoleService;
        this.queryBus = queryBus;
    }

    private String extractToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    /**
     * Authenticates a user and returns a token and user details.
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            String token = authService.login(request.username(), request.password());
            Optional<UUID> userIdOpt = sessionService.getUserIdByToken(token);
            
            if (userIdOpt.isPresent()) {
                Optional<UserDTO> userOpt = queryBus.dispatch(new GetUserByIdQuery(userIdOpt.get()), token);
                if (userOpt.isPresent()) {
                    return ApiResponse.success(new LoginResponseDTO(token, mapToResponse(userOpt.get())));
                }
            }
            return ApiResponse.error("Failed to resolve user session", "SESSION_ERROR");
        } catch (AuthenticationException e) {
            return ApiResponse.error(e.getMessage(), "AUTH_FAILED");
        }
    }

    /**
     * Retrieves the current authenticated user's details.
     */
    @GetMapping("/me")
    public ApiResponse<UserResponseDTO> me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractToken(authHeader);
        if (token == null) {
            return ApiResponse.error("Missing authentication token", "UNAUTHORIZED");
        }

        Optional<UUID> userIdOpt = sessionService.getUserIdByToken(token);
        if (userIdOpt.isPresent()) {
            Optional<UserDTO> userOpt = queryBus.dispatch(new GetUserByIdQuery(userIdOpt.get()), token);
            if (userOpt.isPresent()) {
                return ApiResponse.success(mapToResponse(userOpt.get()));
            }
        }
        
        return ApiResponse.error("Invalid session", "UNAUTHORIZED");
    }

    /**
     * Invalidates the provided authentication token.
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractToken(authHeader);
        if (token != null) {
            authService.logout(token);
        }
        return ApiResponse.success(null);
    }

    private UserResponseDTO mapToResponse(UserDTO u) {
        RoleDTO role = userRoleService.getRoleId(u.id())
                .flatMap(userRoleService::getRole)
                .orElse(null);
        return new UserResponseDTO(u.id(), u.username(), role);
    }
}
