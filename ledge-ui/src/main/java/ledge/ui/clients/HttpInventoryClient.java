package ledge.ui.clients;

import com.google.gson.reflect.TypeToken;
import ledge.api.inventory.dto.request.CreateProductRequestDTO;
import ledge.api.inventory.dto.request.UpdateProductRequestDTO;
import ledge.api.inventory.dto.response.ProductResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

/**
 * Client for inventory-related endpoints.
 * Updated to match the new /products route and PathVariables.
 */
public class HttpInventoryClient extends ApiClient {

    public ApiResponse<List<ProductResponseDTO>> getAllProducts(AuthContext context) {
        Type type = new TypeToken<ApiResponse<List<ProductResponseDTO>>>() {
        }.getType();
        return get("/products", context.token(), type);
    }

    public ApiResponse<Void> createProduct(AuthContext context, CreateProductRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return post("/products", request, context.token(), type);
    }

    public ApiResponse<Void> updateProduct(AuthContext context, UUID productId, UpdateProductRequestDTO request) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return put("/products/" + productId, request, context.token(), type);
    }

    public ApiResponse<Void> deleteProduct(AuthContext context, UUID productId) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return delete("/products/" + productId, null, context.token(), type);
    }
}
