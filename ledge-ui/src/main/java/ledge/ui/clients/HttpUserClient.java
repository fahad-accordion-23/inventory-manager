package ledge.ui.clients;

import com.google.gson.reflect.TypeToken;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.api.users.dto.request.ChangePasswordRequestDTO;
import ledge.api.users.dto.request.ChangeUsernameRequestDTO;
import ledge.api.users.dto.request.CreateUserRequestDTO;
import ledge.api.users.dto.response.UserListResponseDTO;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Client for user-related endpoints.
 * Updated to match the new REST paths using PathVariables.
 */
public class HttpUserClient extends ApiClient {

    public ApiResponse<UserListResponseDTO> getAllUsers(AuthContext context) {
        Type type = new TypeToken<ApiResponse<UserListResponseDTO>>() {
        }.getType();
        return get("/users", context.token(), type);
    }

    public ApiResponse<Void> createUser(AuthContext context, CreateUserRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return post("/users", request, context.token(), type);
    }

    public ApiResponse<Void> changeUsername(AuthContext context, UUID userId, ChangeUsernameRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return put("/users/" + userId + "/username", request, context.token(), type);
    }

    public ApiResponse<Void> changePassword(AuthContext context, UUID userId, ChangePasswordRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return put("/users/" + userId + "/password", request, context.token(), type);
    }

    public ApiResponse<Void> deleteUser(AuthContext context, UUID userId) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return delete("/users/" + userId, null, context.token(), type);
    }
}
