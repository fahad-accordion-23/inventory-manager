package ledge.inventory.readmodel.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import ledge.inventory.writemodel.domain.Product;

public record ProductDTO(
        UUID id,
        String name,
        BigDecimal purchasePrice,
        BigDecimal sellingPrice,
        int stockQuantity,
        BigDecimal taxRate) {

    public static ProductDTO fromProduct(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPurchasePrice(),
                product.getSellingPrice(),
                product.getStockQuantity(),
                product.getTaxRate());
    }
}
