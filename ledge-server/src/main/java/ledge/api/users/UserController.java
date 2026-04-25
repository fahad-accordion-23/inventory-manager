package ledge.api.users;

import ledge.api.shared.ApiResponse;
import ledge.api.shared.ContractMapper;
import ledge.api.users.dto.request.*;
import ledge.api.users.dto.response.GetAllUsersResponseDTO;
import ledge.api.users.dto.UserResponseDTO;
import ledge.security.api.IUserRoleService;
import ledge.security.api.dto.RoleDTO;
import ledge.users.readmodel.contracts.GetAllUsersQuery;
import ledge.users.readmodel.contracts.GetUserByUsernameQuery;
import ledge.users.readmodel.contracts.GetUserByIdQuery;
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
     * Lists all registered users.
     */
    @GetMapping
    public ApiResponse<GetAllUsersResponseDTO> getAllUsers(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractToken(authHeader);
        List<UserDTO> users = queryBus.dispatch(new GetAllUsersQuery(), token);
        List<UserResponseDTO> responseList = users.stream()
                .map(u -> mapToContract(u, token))
                .collect(Collectors.toList());
        return ApiResponse.success(new GetAllUsersResponseDTO(responseList));
    }

    /**
     * Retrieves a specific user by ID.
     */
    @GetMapping("/{id}")
    public ApiResponse<UserResponseDTO> getUserById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID id) {
        String token = extractToken(authHeader);
        return queryBus.dispatch(new GetUserByIdQuery(id), token)
                .map(u -> ApiResponse.success(mapToContract(u, token)))
                .orElse(ApiResponse.error("User not found", "NOT_FOUND"));
    }

    /**
     * Creates a new user account.
     */
    @PostMapping
    public ApiResponse<UserResponseDTO> createUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CreateUserRequestDTO request) {
        String token = extractToken(authHeader);
        commandBus.dispatch(new AddUserCommand(
                request.username(),
                request.password()), token);

        // Fetch the newly created user to return it
        return queryBus.dispatch(new GetUserByUsernameQuery(request.username()), token)
                .map(u -> ApiResponse.success(mapToContract(u, token)))
                .orElse(ApiResponse.error("Failed to retrieve created user", "INTERNAL_ERROR"));
    }

    /**
     * Updates the username of an existing user.
     */
    @PatchMapping("/{id}/username")
    public ApiResponse<Void> updateUsername(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID id,
            @RequestBody ChangeUsernameRequestDTO request) {
        commandBus.dispatch(new ChangeUsernameCommand(
                id,
                request.newUsername()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Updates the password of an existing user.
     */
    @PatchMapping("/{id}/password")
    public ApiResponse<Void> updatePassword(
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

    private UserResponseDTO mapToContract(UserDTO u, String token) {
        RoleDTO internalRole = userRoleService.getRoleId(u.id())
                .flatMap(userRoleService::getRole)
                .orElse(null);
        return ContractMapper.mapUser(u, internalRole);
    }
}
