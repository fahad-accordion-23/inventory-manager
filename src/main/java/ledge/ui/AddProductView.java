package ledge.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddProductView {

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
        System.out.println("Add Product button clicked! Form data:");
        System.out.println("Name: " + nameField.getText());
        System.out.println("Purchase Price: " + purchasePriceField.getText());
        System.out.println("Selling Price: " + sellingPriceField.getText());
        System.out.println("Stock: " + stockField.getText());
        System.out.println("Tax: " + taxField.getText());
    }
}
