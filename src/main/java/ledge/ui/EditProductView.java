package ledge.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import ledge.application.InventoryEventBroker;
import ledge.application.dto.ProductDTO;
import ledge.application.event.ProductUpdatedEvent;

import java.math.BigDecimal;
import java.util.UUID;

public class EditProductView {

    private final InventoryEventBroker eventBroker;
    private final Runnable onCancel;
    private UUID productId;

    @FXML
    private TextField nameField;

    @FXML
    private TextField purchasePriceField;

    @FXML
    private TextField sellingPriceField;

    @FXML
    private TextField stockField;

    @FXML
    private TextField taxField;

    public EditProductView(InventoryEventBroker eventBroker, Runnable onCancel) {
        this.eventBroker = eventBroker;
        this.onCancel = onCancel;
    }

    /** Pre-populate the form with an existing product's data. Call after FXML load. */
    public void setProduct(ProductDTO product) {
        this.productId = product.getId();
        nameField.setText(product.getName());
        purchasePriceField.setText(product.getPurchasePrice().toPlainString());
        sellingPriceField.setText(product.getSellingPrice().toPlainString());
        stockField.setText(String.valueOf(product.getStockQuantity()));
        BigDecimal tax = product.getTaxRate();
        taxField.setText(tax != null ? tax.toPlainString() : "");
    }

    @FXML
    public void handleSave() {
        FormValidator v = new FormValidator();

        String name = v.requireNonBlank(nameField, "Product name");
        BigDecimal purchasePrice = v.requirePositiveDecimal(purchasePriceField, "Purchase price");
        BigDecimal sellingPrice = v.requirePositiveDecimal(sellingPriceField, "Selling price");
        int stock = v.requireNonNegativeInt(stockField, "Stock quantity");
        BigDecimal taxRate = v.optionalDecimalInRange(taxField, "Tax rate", BigDecimal.ZERO, BigDecimal.ONE);

        if (v.hasErrors()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please fix the following:");
            alert.setContentText(v.getErrorSummary());
            alert.showAndWait();
            return;
        }

        ProductDTO dto = new ProductDTO(productId, name, purchasePrice, sellingPrice, stock, taxRate);
        eventBroker.publish(new ProductUpdatedEvent(dto));

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Product successfully updated!");
        alert.showAndWait();

        onCancel.run(); // Navigate back to dashboard
    }

    @FXML
    public void handleCancel() {
        onCancel.run();
    }
}
