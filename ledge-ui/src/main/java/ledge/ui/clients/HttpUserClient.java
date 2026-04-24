package ledge.ui.clients;

import com.google.gson.reflect.TypeToken;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.api.users.dto.request.*;
import ledge.api.users.dto.response.UserListResponseDTO;
import ledge.api.users.dto.response.UserResponseDTO;

import java.lang.reflect.Type;

public class HttpUserClient extends ApiClient {

    public ApiResponse<UserListResponseDTO> getAllUsers(AuthContext context) {
        Type type = new TypeToken<ApiResponse<UserListResponseDTO>>() {
        }.getType();
        return get("/users", context.token(), type);
    }

    public ApiResponse<UserResponseDTO> createUser(AuthContext context, CreateUserRequestDTO request) {
        Type type = new TypeToken<ApiResponse<UserResponseDTO>>() {
        }.getType();
        return post("/users/register", request, context.token(), type);
    }

    public ApiResponse<Void> changeUsername(AuthContext context, ChangeUsernameRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return put("/users/username", request, context.token(), type);
    }

    public ApiResponse<Void> changePassword(AuthContext context, ChangePasswordRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return put("/users/password", request, context.token(), type);
    }

    public ApiResponse<Void> changeRole(AuthContext context, ChangeUserRoleRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return put("/users/role", request, context.token(), type);
    }

    public ApiResponse<Void> deleteUser(AuthContext context, DeleteUserRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return delete("/users", request, context.token(), type);
    }
}
