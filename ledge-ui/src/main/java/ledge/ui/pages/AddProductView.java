package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ledge.ui.clients.HttpInventoryClient;
import ledge.api.inventory.dto.request.CreateProductRequestDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.ui.core.SessionManager;
import ledge.ui.util.FormValidator;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Controller for the Add Product view.
 * Handles form validation and dispatches the AddProductCommand.
 */
public class AddProductView {

    private final HttpInventoryClient inventoryController;
    private final SessionManager sessionManager;

    public AddProductView(HttpInventoryClient inventoryController, SessionManager sessionManager) {
        this.inventoryController = inventoryController;
        this.sessionManager = sessionManager;
    }

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

    @FXML
    public void handleAddProduct() {
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

        CreateProductRequestDTO request = new CreateProductRequestDTO(name, purchasePrice, sellingPrice,
                stock, taxRate);

        Optional<AuthContext> authContext = sessionManager.getAuthContext();
        if (authContext.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR, "You are not logged in.");
            alert.showAndWait();
            return;
        }
        ApiResponse<Void> response = inventoryController.createProduct(
                authContext.get(), request);

        if (response.success()) {
            clearFormFields();
        } else {
            Alert alert = new Alert(AlertType.ERROR, response.error().message());
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Product successfully added!");
        alert.showAndWait();
    }

    private void clearFormFields() {
        nameField.clear();
        purchasePriceField.clear();
        sellingPriceField.clear();
        stockField.clear();
        taxField.clear();
    }
}
