package ledge.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainLayout {

    @FXML
    private VBox contentArea;

    @FXML
    public void initialize() {
        // Load the initial view
        showInventoryDashboard();
    }

    @FXML
    public void showAddProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/AddProductView.fxml"));
            Parent addProductView = loader.load();
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(addProductView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showInventoryDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/InventoryDashboard.fxml"));
            Parent dashboardView = loader.load();
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
