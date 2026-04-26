package ledge.ui.clients;

import com.google.gson.reflect.TypeToken;

import ledge.api.auth.dto.request.LoginRequestDTO;
import ledge.api.auth.dto.response.LoginResponseDTO;
import ledge.api.users.dto.UserResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;

import java.lang.reflect.Type;

public class HttpAuthClient extends ApiClient {

    public ApiResponse<LoginResponseDTO> login(LoginRequestDTO request) {
        Type type = new TypeToken<ApiResponse<LoginResponseDTO>>() {
        }.getType();
        return post("/auth/login", request, null, type);
    }

    public ApiResponse<UserResponseDTO> me(AuthContext context) {
        Type type = new TypeToken<ApiResponse<UserResponseDTO>>() {
        }.getType();
        return get("/auth/me", context.token(), type);
    }

    public ApiResponse<Void> logout(AuthContext context) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        String token = context != null ? context.token() : null;
        return post("/auth/logout", null, token, type);
    }
}
