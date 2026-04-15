package ledge.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ledge.application.InventoryCommandBus;
import ledge.application.command.AddProductCommand;
import ledge.application.dto.ProductDTO;

import java.math.BigDecimal;

public class AddProductView {

    private final InventoryCommandBus commandBus;

    public AddProductView(InventoryCommandBus commandBus) {
        this.commandBus = commandBus;
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
            commandBus.dispatch(new AddProductCommand(dto));
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
