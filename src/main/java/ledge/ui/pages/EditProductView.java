package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import ledge.api.inventory.InventoryController;
import ledge.api.inventory.dto.request.UpdateProductRequestDTO;
import ledge.api.inventory.dto.response.ProductResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.ui.core.SessionManager;
import ledge.ui.util.FormValidator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for the Edit Product view.
 * Handles existing product data loading, validation, and update command
 * dispatch via the API layer.
 */
public class EditProductView {

    private final InventoryController inventoryController;
    private final SessionManager sessionManager;
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

    public EditProductView(InventoryController inventoryController, SessionManager sessionManager, Runnable onCancel) {
        this.inventoryController = inventoryController;
        this.sessionManager = sessionManager;
        this.onCancel = onCancel;
    }

    /**
     * Pre-populate the form with an existing product's data. Call after FXML load.
     */
    public void setProduct(ProductResponseDTO product) {
        this.productId = product.id();
        nameField.setText(product.name());
        purchasePriceField.setText(product.purchasePrice().toPlainString());
        sellingPriceField.setText(product.sellingPrice().toPlainString());
        stockField.setText(String.valueOf(product.stockQuantity()));
        BigDecimal tax = product.taxRate();
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

        UpdateProductRequestDTO request = new UpdateProductRequestDTO(
                productId,
                name,
                purchasePrice,
                sellingPrice,
                stock,
                taxRate);

        Optional<AuthContext> authContext = sessionManager.getAuthContext();
        if (authContext.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR, "You are not logged in.");
            alert.showAndWait();
            return;
        }

        ApiResponse<Void> response = inventoryController.updateProduct(authContext.get(), request);

        if (response.success()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Product successfully updated!");
            alert.showAndWait();

            onCancel.run(); // Navigate back to dashboard
        } else {
            Alert alert = new Alert(AlertType.ERROR, response.error().message());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCancel() {
        onCancel.run();
    }
}
