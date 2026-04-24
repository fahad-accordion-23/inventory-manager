package ledge.api.users;

import ledge.api.shared.ApiResponse;
import ledge.api.users.dto.request.*;
import ledge.api.users.dto.response.UserListResponseDTO;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.users.readmodel.contracts.GetAllUsersQuery;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.shared.infrastructure.queries.QueryBus;
import ledge.shared.infrastructure.commands.CommandBus;
import ledge.users.writemodel.commands.*;

import java.util.List;
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

    public UserController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
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
                .map(u -> new UserResponseDTO(u.id(), u.username(), u.role()))
                .collect(Collectors.toList());
        return ApiResponse.success(new UserListResponseDTO(responseList));
    }

    /**
     * Registers a new user.
     */
    @PostMapping("register")
    public ApiResponse<UserResponseDTO> createUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CreateUserRequestDTO request) {
        commandBus.dispatch(new AddUserCommand(
                request.username(),
                request.password(),
                request.role()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Changes a user's username.
     */
    @PutMapping("username")
    public ApiResponse<Void> changeUsername(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody ChangeUsernameRequestDTO request) {
        commandBus.dispatch(new ChangeUsernameCommand(
                request.user_id(),
                request.new_username()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Changes a user's password.
     */
    @PutMapping("password")
    public ApiResponse<Void> changePassword(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody ChangePasswordRequestDTO request) {
        commandBus.dispatch(new ChangeUserPasswordCommand(
                request.user_id(),
                request.new_password()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Changes a user's role.
     */
    @PutMapping("role")
    public ApiResponse<Void> changeRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody ChangeUserRoleRequestDTO request) {
        commandBus.dispatch(new ChangeUserRoleCommand(
                request.user_id(),
                request.new_role()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Deletes a user from the system.
     */
    @DeleteMapping
    public ApiResponse<Void> deleteUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody DeleteUserRequestDTO request) {
        commandBus.dispatch(new RemoveUserCommand(request.user_id()), extractToken(authHeader));
        return ApiResponse.success(null);
    }
}
