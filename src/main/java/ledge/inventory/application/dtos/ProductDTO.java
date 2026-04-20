package ledge.inventory.application.dtos;

import ledge.inventory.domain.Product;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
        UUID id,
        String name,
        BigDecimal purchasePrice,
        BigDecimal sellingPrice,
        int stockQuantity,
        BigDecimal taxRate) {
    public Product toProduct() {
        if (id == null) {
            return Product.register(name, purchasePrice, sellingPrice, stockQuantity, taxRate);
        } else {
            return Product.rehydrate(id, name, purchasePrice, sellingPrice, stockQuantity, taxRate);
        }
    }

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
