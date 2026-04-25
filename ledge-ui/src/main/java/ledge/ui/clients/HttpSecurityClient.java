package ledge.ui.clients;

import com.google.gson.reflect.TypeToken;
import ledge.api.security.dto.AssignRoleRequestDTO;
import ledge.api.security.dto.RegisterRoleRequestDTO;
import ledge.security.api.dto.RoleDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

/**
 * Client for the Security Open Host Service endpoints.
 */
public class HttpSecurityClient extends ApiClient {

    public ApiResponse<List<RoleDTO>> getAllRoles(AuthContext context) {
        Type type = new TypeToken<ApiResponse<List<RoleDTO>>>() {
        }.getType();
        return get("/security/roles", context.token(), type);
    }

    public ApiResponse<UUID> registerRole(AuthContext context, RegisterRoleRequestDTO request) {
        Type type = new TypeToken<ApiResponse<UUID>>() {
        }.getType();
        return post("/security/roles", request, context.token(), type);
    }

    public ApiResponse<Void> assignRole(AuthContext context, UUID userId, AssignRoleRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return put("/security/assignments/" + userId, request, context.token(), type);
    }

    public ApiResponse<Void> removeRole(AuthContext context, UUID userId) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return delete("/security/assignments/" + userId, null, context.token(), type);
    }
}
