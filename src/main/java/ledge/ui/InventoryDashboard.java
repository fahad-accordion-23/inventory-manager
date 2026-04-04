package ledge.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import ledge.application.dto.ProductDTO;

public class InventoryDashboard {

    @FXML
    private TableView<ProductDTO> inventoryTable;

    @FXML
    public void initialize() {
        System.out.println("Inventory Dashboard initialized!");
    }
}
