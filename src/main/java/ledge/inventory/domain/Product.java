package ledge.inventory.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {
    private final UUID id;
    private String name;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private int stockQuantity;
    private BigDecimal taxRate;

    private Product(UUID id, String name, BigDecimal purchasePrice, BigDecimal sellingPrice, int stockQuantity, BigDecimal taxRate) {
        this.id = id;
        
        // Using setters to ensure consistent validation during initialization
        setName(name);
        setPurchasePrice(purchasePrice);
        setSellingPrice(sellingPrice);
        setStockQuantity(stockQuantity);
        setTaxRate(taxRate);
    }

    public static Product register(String name, BigDecimal purchasePrice, BigDecimal sellingPrice, int stockQuantity, BigDecimal taxRate) {
        return new Product(UUID.randomUUID(), name, purchasePrice, sellingPrice, stockQuantity, taxRate);
    }

    public static Product rehydrate(UUID id, String name, BigDecimal purchasePrice, BigDecimal sellingPrice, int stockQuantity, BigDecimal taxRate) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null when rehydrating a product");
        }
        return new Product(id, name, purchasePrice, sellingPrice, stockQuantity, taxRate);
    }

    // ID is immutable; only a getter is provided
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        this.name = name.trim();
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Purchase price must be positive");
        }
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        if (sellingPrice == null || sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Selling price must be positive");
        }
        this.sellingPrice = sellingPrice;
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
        if (taxRate == null) {
            this.taxRate = BigDecimal.ZERO;
            return;
        }
        if (taxRate.compareTo(BigDecimal.ZERO) < 0 || taxRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Tax rate must be between 0 and 1");
        }
        this.taxRate = taxRate;
    }

    @Override
    public String toString() {
        return String.format("Product[ID=%s, Name=%s, Stock=%d, Tax=%s%%]", 
            id, name, stockQuantity, taxRate.multiply(new BigDecimal("100")));
    }
}