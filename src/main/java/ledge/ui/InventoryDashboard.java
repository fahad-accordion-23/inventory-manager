package ledge.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import ledge.domain.Product;

public class InventoryDashboard {

    @FXML
    private TableView<Product> inventoryTable;

    @FXML
    public void initialize() {
        System.out.println("Inventory Dashboard initialized!");
    }
}
