package ledge.ui.clients;

import com.google.gson.reflect.TypeToken;

import ledge.api.security.dto.request.AssignRoleRequestDTO;
import ledge.api.security.dto.request.RegisterRoleRequestDTO;
import ledge.api.security.dto.response.GetAllRolesResponseDTO;
import ledge.api.security.dto.response.GetUserRoleResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Client for the Security Open Host Service endpoints.
 * Aligned with the latest REST paths and return types.
 */
public class HttpSecurityClient extends ApiClient {

    public ApiResponse<GetAllRolesResponseDTO> getAllRoles(AuthContext context) {
        Type type = new TypeToken<ApiResponse<GetAllRolesResponseDTO>>() {
        }.getType();
        return get("/security/roles", context.token(), type);
    }

    public ApiResponse<GetUserRoleResponseDTO> getAssignment(AuthContext context, UUID userId) {
        Type type = new TypeToken<ApiResponse<GetUserRoleResponseDTO>>() {
        }.getType();
        return get("/security/users/" + userId + "/role", context.token(), type);
    }

    public ApiResponse<Void> registerRole(AuthContext context, RegisterRoleRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return post("/security/roles", request, context.token(), type);
    }

    public ApiResponse<Void> assignRole(AuthContext context, UUID userId, AssignRoleRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return put("/security/users/" + userId + "/role", request, context.token(), type);
    }

    public ApiResponse<Void> removeRole(AuthContext context, UUID userId) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return delete("/security/users/" + userId, null, context.token(), type);
    }
}
