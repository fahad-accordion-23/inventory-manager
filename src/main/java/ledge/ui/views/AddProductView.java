package ledge.ui.views;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ledge.inventory.application.commands.AddProductCommand;
import ledge.inventory.application.dtos.ProductDTO;
import ledge.inventory.infrastructure.messaging.InventoryCommandBus;
import ledge.ui.SessionManager;

import java.math.BigDecimal;

/**
 * Controller for the Add Product view.
 * Handles form validation and dispatches the AddProductCommand.
 */
public class AddProductView {

    private final InventoryCommandBus commandBus;
    private final SessionManager sessionManager;

    public AddProductView(InventoryCommandBus commandBus, SessionManager sessionManager) {
        this.commandBus = commandBus;
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

        ProductDTO dto = new ProductDTO(null, name, purchasePrice, sellingPrice, stock, taxRate);
        try {
            String token = sessionManager.getAuthToken().orElse("");
            commandBus.dispatch(new AddProductCommand(dto), token);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFormFields();

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
