package ledge.api.products;

import ledge.api.products.dto.request.*;
import ledge.api.products.dto.response.ProductResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.inventory.readmodel.contracts.GetAllProductsQuery;
import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.inventory.readmodel.infrastructure.messaging.InventoryQueryBus;
import ledge.inventory.writemodel.contracts.AddProductCommand;
import ledge.inventory.writemodel.contracts.RemoveProductCommand;
import ledge.inventory.writemodel.contracts.UpdateProductCommand;
import ledge.inventory.writemodel.infrastructure.messaging.InventoryCommandBus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing inventory/product-related API requests.
 */
public class ProductController {
    private final InventoryCommandBus commandBus;
    private final InventoryQueryBus queryBus;

    public ProductController(InventoryCommandBus commandBus, InventoryQueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    /**
     * Retrieves all products in the inventory.
     */
    public ApiResponse<List<ProductResponseDTO>> getAllProducts(AuthContext context) {
        List<ProductDTO> products = queryBus.dispatch(new GetAllProductsQuery(), context.token());
        List<ProductResponseDTO> responseList = products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(responseList);
    }

    /**
     * Adds a new product to the inventory.
     */
    public ApiResponse<ProductResponseDTO> createProduct(AuthContext context, CreateProductRequestDTO request) {
        ProductDTO dto = new ProductDTO(
                null,
                request.name(),
                request.purchasePrice(),
                request.sellingPrice(),
                request.stockQuantity(),
                request.taxRate());

        commandBus.dispatch(new AddProductCommand(dto), context.token());
        return ApiResponse.success(null);
    }

    /**
     * Updates an existing product's details.
     */
    public ApiResponse<Void> updateProduct(AuthContext context, UpdateProductRequestDTO request) {
        ProductDTO dto = new ProductDTO(
                request.id(),
                request.name(),
                request.purchasePrice(),
                request.sellingPrice(),
                request.stockQuantity(),
                request.taxRate());

        commandBus.dispatch(new UpdateProductCommand(dto), context.token());
        return ApiResponse.success(null);
    }

    /**
     * Removes a product from the inventory.
     */
    public ApiResponse<Void> deleteProduct(AuthContext context, DeleteProductRequestDTO request) {
        commandBus.dispatch(new RemoveProductCommand(request.id()), context.token());
        return ApiResponse.success(null);
    }

    private ProductResponseDTO mapToResponse(ProductDTO dto) {
        return new ProductResponseDTO(
                dto.id(),
                dto.name(),
                dto.purchasePrice(),
                dto.sellingPrice(),
                dto.stockQuantity(),
                dto.taxRate());
    }
}
