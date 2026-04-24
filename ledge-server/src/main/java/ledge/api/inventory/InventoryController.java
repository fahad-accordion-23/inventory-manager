package ledge.api.inventory;

import ledge.api.inventory.dto.request.*;
import ledge.api.inventory.dto.response.ProductResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.inventory.readmodel.contracts.GetAllProductsQuery;
import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.inventory.writemodel.contracts.AddProductCommand;
import ledge.inventory.writemodel.contracts.RemoveProductCommand;
import ledge.inventory.writemodel.contracts.UpdateProductCommand;
import ledge.shared.infrastructure.queries.QueryBus;
import ledge.shared.infrastructure.commands.CommandBus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing inventory/product-related API requests.
 */
@RestController
@RequestMapping("/api/products")
public class InventoryController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    public InventoryController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    private String extractToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    /**
     * Retrieves all products in the inventory.
     */
    @GetMapping
    public ApiResponse<List<ProductResponseDTO>> getAllProducts(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        List<ProductDTO> products = queryBus.dispatch(new GetAllProductsQuery(), extractToken(authHeader));
        List<ProductResponseDTO> responseList = products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(responseList);
    }

    /**
     * Adds a new product to the inventory.
     */
    @PostMapping
    public ApiResponse<Void> createProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CreateProductRequestDTO request) {
        commandBus.dispatch(new AddProductCommand(
                request.name(),
                "", // description (optional or separate field)
                request.purchasePrice(),
                request.sellingPrice(),
                request.stockQuantity(),
                request.taxRate()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Updates an existing product's details.
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> updateProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID id,
            @RequestBody UpdateProductRequestDTO request) {
        commandBus.dispatch(new UpdateProductCommand(
                id,
                request.name(),
                "", // description
                request.purchasePrice(),
                request.sellingPrice(),
                request.stockQuantity(),
                request.taxRate()), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    /**
     * Removes a product from the inventory.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID id) {
        commandBus.dispatch(new RemoveProductCommand(id), extractToken(authHeader));
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
