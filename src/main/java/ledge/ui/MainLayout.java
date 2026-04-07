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
    
    private Parent dashboardViewCache;
    private Parent addProductViewCache;

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
        if (addProductViewCache == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/AddProductView.fxml"));
                loader.setControllerFactory(param -> new AddProductView(eventBroker));
                addProductViewCache = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(addProductViewCache);
    }

    @FXML
    public void showInventoryDashboard() {
        if (dashboardViewCache == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/InventoryDashboard.fxml"));
                loader.setControllerFactory(param -> new InventoryDashboard(eventBroker));
                dashboardViewCache = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardViewCache);
    }
}
