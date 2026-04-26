package ledge.api.inventory;

import ledge.api.inventory.dto.request.*;
import ledge.api.inventory.dto.response.GetAllProductsResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.inventory.readmodel.contracts.GetAllProductsQuery;
import ledge.shared.infrastructure.queries.QueryBus;
import ledge.shared.infrastructure.commands.CommandBus;
import ledge.inventory.writemodel.contracts.AddProductCommand;
import ledge.inventory.writemodel.contracts.RemoveProductCommand;
import ledge.inventory.writemodel.contracts.UpdateProductCommand;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing inventory/product-related API requests.
 * Aligned with the latest API documentation and simplified ProductDTO.
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
     * Lists all products.
     */
    @GetMapping
    public ApiResponse<GetAllProductsResponseDTO> getAllProducts(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractToken(authHeader);
        List<ledge.inventory.readmodel.dtos.ProductDTO> products = queryBus.dispatch(new GetAllProductsQuery(), token);
        List<ledge.api.inventory.dto.ProductResponseDTO> responseList = products.stream()
                .map(this::mapToContract)
                .collect(Collectors.toList());
        return ApiResponse.success(new GetAllProductsResponseDTO(responseList));
    }

    /**
     * Adds a new product to the inventory.
     */
    @PostMapping
    public ApiResponse<ledge.api.inventory.dto.ProductResponseDTO> createProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CreateProductRequestDTO request) {
        String token = extractToken(authHeader);
        commandBus.dispatch(new AddProductCommand(
                request.name(),
                request.purchasePrice(),
                request.sellingPrice(),
                request.stockQuantity(),
                request.taxRate()), token);

        // Fetch the list to find the newly created product (by name as ID is unknown)
        return queryBus.dispatch(new GetAllProductsQuery(), token).stream()
                .filter(p -> p.name().equals(request.name()))
                .findFirst()
                .map(p -> ApiResponse.success(mapToContract(p)))
                .orElse(ApiResponse.error("Failed to retrieve created product", "INTERNAL_ERROR"));
    }

    /**
     * Updates an existing product's details.
     */
    @PutMapping("/{id}")
    public ApiResponse<ledge.api.inventory.dto.ProductResponseDTO> updateProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("id") UUID id,
            @RequestBody UpdateProductRequestDTO request) {
        String token = extractToken(authHeader);
        commandBus.dispatch(new UpdateProductCommand(
                id,
                request.name(),
                request.purchasePrice(),
                request.sellingPrice(),
                request.stockQuantity(),
                request.taxRate()), token);

        // Fetch the updated product
        return queryBus.dispatch(new GetAllProductsQuery(), token).stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .map(p -> ApiResponse.success(mapToContract(p)))
                .orElse(ApiResponse.error("Product not found after update", "NOT_FOUND"));
    }

    /**
     * Removes a product from the inventory.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("id") UUID id) {
        commandBus.dispatch(new RemoveProductCommand(id), extractToken(authHeader));
        return ApiResponse.success(null);
    }

    private ledge.api.inventory.dto.ProductResponseDTO mapToContract(ledge.inventory.readmodel.dtos.ProductDTO dto) {
        return new ledge.api.inventory.dto.ProductResponseDTO(
                dto.id(),
                dto.name(),
                dto.purchasePrice(),
                dto.sellingPrice(),
                dto.stockQuantity(),
                dto.taxRate());
    }
}
