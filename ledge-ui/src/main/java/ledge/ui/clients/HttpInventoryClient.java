package ledge.ui.clients;

import com.google.gson.reflect.TypeToken;
import ledge.api.inventory.dto.request.*;
import ledge.api.inventory.dto.response.ProductResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;

import java.lang.reflect.Type;
import java.util.List;

public class HttpInventoryClient extends ApiClient {

    public ApiResponse<List<ProductResponseDTO>> getAllProducts(AuthContext context) {
        Type type = new TypeToken<ApiResponse<List<ProductResponseDTO>>>() {
        }.getType();
        return get("/inventory", context.token(), type);
    }

    public ApiResponse<ProductResponseDTO> createProduct(AuthContext context, CreateProductRequestDTO request) {
        Type type = new TypeToken<ApiResponse<ProductResponseDTO>>() {
        }.getType();
        return post("/inventory", request, context.token(), type);
    }

    public ApiResponse<Void> updateProduct(AuthContext context, UpdateProductRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return put("/inventory", request, context.token(), type);
    }

    public ApiResponse<Void> deleteProduct(AuthContext context, DeleteProductRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return delete("/inventory", request, context.token(), type);
    }
}
