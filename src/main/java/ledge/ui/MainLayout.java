package ledge.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import ledge.application.InventoryEventBroker;
import ledge.application.event.InventoryRefreshRequestedEvent;
import ledge.application.event.ProductAddedEvent;

import java.io.IOException;
import java.util.List;

public class MainLayout {

    @FXML
    private VBox contentArea;

    @FXML
    private Sidebar sidebarController;

    private final InventoryEventBroker eventBroker;

    public MainLayout(InventoryEventBroker eventBroker) {
        this.eventBroker = eventBroker;
    }

    @FXML
    public void initialize() {
        List<NavItem> navItems = List.of(
                new NavItem("Inventory Dashboard", InventoryRefreshRequestedEvent.REQUIRED,
                        this::showInventoryDashboard),
                new NavItem("Add Product", ProductAddedEvent.REQUIRED, this::showAddProduct));
        sidebarController.setNavItems(navItems);
    }

    @FXML
    public void showAddProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/AddProductView.fxml"));
            loader.setControllerFactory(param -> new AddProductView(eventBroker));
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
            loader.setControllerFactory(param -> new InventoryDashboard(eventBroker));
            Parent dashboardView = loader.load();

            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
