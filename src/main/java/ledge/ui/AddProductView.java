package ledge.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ledge.application.InventoryEventBroker;
import ledge.application.ProductAddedEvent;
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
            String name = nameField.getText();
            BigDecimal purchasePrice = new BigDecimal(purchasePriceField.getText());
            BigDecimal sellingPrice = new BigDecimal(sellingPriceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            BigDecimal taxRate = new BigDecimal(taxField.getText());

            ProductDTO dto = new ProductDTO(null, name, purchasePrice, sellingPrice, stock, taxRate);
            eventBroker.publish(new ProductAddedEvent(dto));
            
            // Clear fields
            nameField.clear();
            purchasePriceField.clear();
            sellingPriceField.clear();
            stockField.clear();
            taxField.clear();
        } catch (Exception e) {
            System.err.println("Failed to parse form inputs: " + e.getMessage());
        }
    }
}
