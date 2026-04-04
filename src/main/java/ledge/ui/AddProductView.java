package ledge.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ledge.application.InventoryEventBroker;
import ledge.application.event.ProductAddedEvent;
import ledge.application.dto.ProductDTO;

import java.math.BigDecimal;

public class AddProductView {

    private final InventoryEventBroker eventBroker;

    public AddProductView(InventoryEventBroker eventBroker) {
        this.eventBroker = eventBroker;
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
        try {
            ProductDTO dto = createProductFromInput();
            eventBroker.publish(new ProductAddedEvent(dto));
            clearFormFields();
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Product successfully added!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please check your numbers and try again.\nError: " + e.getMessage());
            alert.showAndWait();
            System.err.println("Failed to parse form inputs: " + e.getMessage());
        }
    }

    private ProductDTO createProductFromInput() {
        String name = nameField.getText();
        BigDecimal purchasePrice = new BigDecimal(purchasePriceField.getText());
        BigDecimal sellingPrice = new BigDecimal(sellingPriceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        
        String taxText = taxField.getText();
        BigDecimal taxRate = (taxText == null || taxText.trim().isEmpty()) ? null : new BigDecimal(taxText);

        return new ProductDTO(null, name, purchasePrice, sellingPrice, stock, taxRate);
    }

    private void clearFormFields() {
        nameField.clear();
        purchasePriceField.clear();
        sellingPriceField.clear();
        stockField.clear();
        taxField.clear();
    }
}
