package ledge.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductDTO {
    private final UUID id;
    private final String name;
    private final BigDecimal purchasePrice;
    private final BigDecimal sellingPrice;
    private final int stockQuantity;
    private final BigDecimal taxRate;

    public ProductDTO(UUID id, String name, BigDecimal purchasePrice, BigDecimal sellingPrice, int stockQuantity, BigDecimal taxRate) {
        this.id = id;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.taxRate = taxRate;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public BigDecimal getSellingPrice() { return sellingPrice; }
    public int getStockQuantity() { return stockQuantity; }
    public BigDecimal getTaxRate() { return taxRate; }
}
