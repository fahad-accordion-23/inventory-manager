package ledge.api.inventory.dto.response;

import java.util.List;

import ledge.api.inventory.dto.ProductResponseDTO;

public record GetAllProductsResponseDTO(List<ProductResponseDTO> products) {
}
