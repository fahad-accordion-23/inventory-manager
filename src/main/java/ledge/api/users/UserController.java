package ledge.api.users;

import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.api.users.dto.request.*;
import ledge.api.users.dto.response.UserListResponseDTO;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.users.readmodel.contracts.GetAllUsersQuery;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.messaging.UserQueryBus;
import ledge.users.writemodel.commands.*;
import ledge.users.writemodel.infrastructure.messaging.UserCommandBus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing user-related API requests.
 */
public class UserController {
    private final UserCommandBus commandBus;
    private final UserQueryBus queryBus;

    public UserController(UserCommandBus commandBus, UserQueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    /**
     * Retrieves all users in the system.
     */
    public ApiResponse<UserListResponseDTO> getAllUsers(AuthContext context) {
        List<UserDTO> users = queryBus.dispatch(new GetAllUsersQuery(), context.token());
        List<UserResponseDTO> responseList = users.stream()
                .map(u -> new UserResponseDTO(u.id(), u.username(), u.role()))
                .collect(Collectors.toList());
        return ApiResponse.success(new UserListResponseDTO(responseList));
    }

    /**
     * Registers a new user.
     */
    public ApiResponse<UserResponseDTO> createUser(AuthContext context, CreateUserRequestDTO request) {
        commandBus.dispatch(new AddUserCommand(
                request.username(),
                request.password(),
                request.role()), context.token());
        return ApiResponse.success(null);
    }

    /**
     * Changes a user's username.
     */
    public ApiResponse<Void> changeUsername(AuthContext context, ChangeUsernameRequestDTO request) {
        commandBus.dispatch(new ChangeUsernameCommand(
                request.user_id(),
                request.new_username()), context.token());
        return ApiResponse.success(null);
    }

    /**
     * Changes a user's password.
     */
    public ApiResponse<Void> changePassword(AuthContext context, ChangePasswordRequestDTO request) {
        commandBus.dispatch(new ChangeUserPasswordCommand(
                request.user_id(),
                request.new_password()), context.token());
        return ApiResponse.success(null);
    }

    /**
     * Changes a user's role.
     */
    public ApiResponse<Void> changeRole(AuthContext context, ChangeUserRoleRequestDTO request) {
        commandBus.dispatch(new ChangeUserRoleCommand(
                request.user_id(),
                request.new_role()), context.token());
        return ApiResponse.success(null);
    }

    /**
     * Deletes a user from the system.
     */
    public ApiResponse<Void> deleteUser(AuthContext context, DeleteUserRequestDTO request) {
        commandBus.dispatch(new RemoveUserCommand(request.user_id()), context.token());
        return ApiResponse.success(null);
    }
}
