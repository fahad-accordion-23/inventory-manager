package ledge.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Product {
    private final UUID id;
    private String name;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private int stockQuantity;
    private BigDecimal taxRate;

    public Product(String name, BigDecimal purchasePrice, BigDecimal sellingPrice, int stockQuantity, BigDecimal taxRate) {
        // US-2.1: Automatic generation of ID
        this.id = UUID.randomUUID();
        
        // Using setters to ensure consistent validation during initialization
        setName(name);
        setPurchasePrice(purchasePrice);
        setSellingPrice(sellingPrice);
        setStockQuantity(stockQuantity);
        setTaxRate(taxRate);
    }

    // ID is immutable; only a getter is provided
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Requirement: isNotNull check
        this.name = Objects.requireNonNull(name, "Product name cannot be null");
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        // Requirement: isNotNull check
        this.purchasePrice = Objects.requireNonNull(purchasePrice, "Purchase price cannot be null");
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        // Requirement: isNotNull check
        this.sellingPrice = Objects.requireNonNull(sellingPrice, "Selling price cannot be null");
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        // Requirement: if isNull for tax, set to 0%
        this.taxRate = (taxRate == null) ? BigDecimal.ZERO : taxRate;
    }

    @Override
    public String toString() {
        return String.format("Product[ID=%s, Name=%s, Stock=%d, Tax=%s%%]", 
            id, name, stockQuantity, taxRate.multiply(new BigDecimal("100")));
    }
}