package ledge.api.users;

import ledge.api.shared.ApiResponse;
import ledge.api.users.dto.request.*;
import ledge.api.users.dto.response.UserListResponseDTO;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.security.api.IUserRoleService;
import ledge.security.api.dto.RoleDTO;
import ledge.users.readmodel.contracts.GetAllUsersQuery;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.shared.infrastructure.queries.QueryBus;
import ledge.shared.infrastructure.commands.CommandBus;
import ledge.users.writemodel.commands.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing user-related API requests.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final IUserRoleService userRoleService;

    public UserController(CommandBus commandBus, QueryBus queryBus, IUserRoleService userRoleService) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.userRoleService = userRoleService;
    }

    private String extractToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    /**
     * Retrieves all users in the system.
     */
    @GetMapping
    public ApiResponse<UserListResponseDTO> getAllUsers(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        List<UserDTO> users = queryBus.dispatch(new GetAllUsersQuery(), extractToken(authHeader));
        List<UserResponseDTO> responseList = users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(new UserListResponseDTO(responseList));
    }

    /**
     * Registers a new user.
     */
    @PostMapping("/register")
    public ApiResponse<Void> createUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CreateUserRequestDTO request) {
        commandBus.dispatch(new AddUserCommand(
                request.username(),
                request.password()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Changes a user's username.
     */
    @PutMapping("/{id}/username")
    public ApiResponse<Void> changeUsername(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID id,
            @RequestBody ChangeUsernameRequestDTO request) {
        commandBus.dispatch(new ChangeUsernameCommand(
                id,
                request.newUsername()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Changes a user's password.
     */
    @PutMapping("/{id}/password")
    public ApiResponse<Void> changePassword(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID id,
            @RequestBody ChangePasswordRequestDTO request) {
        commandBus.dispatch(new ChangeUserPasswordCommand(
                id,
                request.newPassword()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Deletes a user from the system.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID id) {
        commandBus.dispatch(new RemoveUserCommand(id), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    private UserResponseDTO mapToResponse(UserDTO u) {
        RoleDTO role = userRoleService.getRoleId(u.id())
                .flatMap(userRoleService::getRole)
                .orElse(null);
        return new UserResponseDTO(u.id(), u.username(), role);
    }
}
