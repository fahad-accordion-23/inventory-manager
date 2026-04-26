package ledge.ui.viewmodels;

import javafx.beans.property.*;
import ledge.api.inventory.dto.ProductResponseDTO;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * JavaFX-friendly view model for Product.
 * Aligned with the consolidated API contract package.
 */
public class ProductViewModel {
    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> purchasePrice = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> sellingPrice = new SimpleObjectProperty<>();
    private final IntegerProperty stockQuantity = new SimpleIntegerProperty();
    private final ObjectProperty<BigDecimal> taxRate = new SimpleObjectProperty<>();

    public ProductViewModel() {
    }

    public ProductViewModel(ProductResponseDTO dto) {
        updateFrom(dto);
    }

    public static ProductViewModel fromDTO(ProductResponseDTO dto) {
        return new ProductViewModel(dto);
    }

    public void updateFrom(ProductResponseDTO dto) {
        this.id.set(dto.id());
        this.name.set(dto.name());
        this.purchasePrice.set(dto.purchasePrice());
        this.sellingPrice.set(dto.sellingPrice());
        this.stockQuantity.set(dto.stockQuantity());
        this.taxRate.set(dto.taxRate());
    }

    public ProductResponseDTO toDTO() {
        return new ProductResponseDTO(
                id.get(),
                name.get(),
                purchasePrice.get(),
                sellingPrice.get(),
                stockQuantity.get(),
                taxRate.get());
    }

    // --- Properties ---

    public ObjectProperty<UUID> idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObjectProperty<BigDecimal> purchasePriceProperty() {
        return purchasePrice;
    }

    public ObjectProperty<BigDecimal> sellingPriceProperty() {
        return sellingPrice;
    }

    public IntegerProperty stockQuantityProperty() {
        return stockQuantity;
    }

    public ObjectProperty<BigDecimal> taxRateProperty() {
        return taxRate;
    }

    // --- Accessors ---

    public UUID getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice.get();
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice.get();
    }

    public int getStockQuantity() {
        return stockQuantity.get();
    }

    public BigDecimal getTaxRate() {
        return taxRate.get();
    }

    public void setId(UUID id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice.set(purchasePrice);
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice.set(sellingPrice);
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity.set(stockQuantity);
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate.set(taxRate);
    }
}
