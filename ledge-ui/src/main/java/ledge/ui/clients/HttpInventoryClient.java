package ledge.ui.clients;

import com.google.gson.reflect.TypeToken;
import ledge.api.inventory.dto.request.CreateProductRequestDTO;
import ledge.api.inventory.dto.request.UpdateProductRequestDTO;
import ledge.api.inventory.dto.ProductResponseDTO;
import ledge.api.inventory.dto.response.GetAllProductsResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Client for inventory-related endpoints.
 * Aligned with the latest API contracts and return types.
 */
public class HttpInventoryClient extends ApiClient {

    public ApiResponse<GetAllProductsResponseDTO> getAllProducts(AuthContext context) {
        Type type = new TypeToken<ApiResponse<GetAllProductsResponseDTO>>() {
        }.getType();
        return get("/products", context.token(), type);
    }

    public ApiResponse<ProductResponseDTO> createProduct(AuthContext context, CreateProductRequestDTO request) {
        Type type = new TypeToken<ApiResponse<ProductResponseDTO>>() {
        }.getType();
        return post("/products", request, context.token(), type);
    }

    public ApiResponse<ProductResponseDTO> updateProduct(AuthContext context, UUID productId, UpdateProductRequestDTO request) {
        Type type = new TypeToken<ApiResponse<ProductResponseDTO>>() {
        }.getType();
        return put("/products/" + productId, request, context.token(), type);
    }

    public ApiResponse<Void> deleteProduct(AuthContext context, UUID productId) {
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        return delete("/products/" + productId, null, context.token(), type);
    }
}
