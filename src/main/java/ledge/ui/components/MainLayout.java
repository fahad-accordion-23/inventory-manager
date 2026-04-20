package ledge.ui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import ledge.inventory.application.dtos.ProductDTO;
import ledge.inventory.infrastructure.messaging.InventoryCommandBus;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.inventory.infrastructure.messaging.InventoryQueryBus;
import ledge.ui.core.Capability;
import ledge.ui.core.SessionManager;
import ledge.ui.messaging.UIEventBroker;
import ledge.ui.pages.AddProductView;
import ledge.ui.pages.EditProductView;
import ledge.ui.pages.InventoryDashboard;

import java.io.IOException;

/**
 * Controller for the main application layout.
 * Manages the sidebar and content area, facilitating navigation between
 * internal views.
 */
public class MainLayout {

    @FXML
    private VBox contentArea;

    @FXML
    private Sidebar sidebarController;

    private final InventoryEventBroker inventoryEventBroker;
    private final InventoryCommandBus commandBus;
    private final InventoryQueryBus queryBus;
    private final SessionManager sessionManager;
    private final UIEventBroker uiEventBroker;

    private Parent dashboardViewCache;
    private Parent addProductViewCache;

    public MainLayout(InventoryEventBroker inventoryEventBroker,
            InventoryCommandBus commandBus,
            InventoryQueryBus queryBus,
            SessionManager sessionManager,
            UIEventBroker uiEventBroker) {
        this.inventoryEventBroker = inventoryEventBroker;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.sessionManager = sessionManager;
        this.uiEventBroker = uiEventBroker;
    }

    @FXML
    public void initialize() {
        sidebarController.clearNavItems();
        sidebarController.addNavItem("Inventory Dashboard", Capability.VIEW_DASHBOARD, this::showInventoryDashboard);
        sidebarController.addNavItem("Add Product", Capability.CREATE_PRODUCT, this::showAddProduct);
    }

    @FXML
    public void showAddProduct() {
        if (addProductViewCache == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/AddProductView.fxml"));
                loader.setControllerFactory(param -> new AddProductView(commandBus, sessionManager));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/InventoryDashboard.fxml"));
                loader.setControllerFactory(
                        param -> new InventoryDashboard(inventoryEventBroker, commandBus, queryBus, sessionManager,
                                this::showEditProduct));
                dashboardViewCache = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(dashboardViewCache);
    }

    public void showEditProduct(ProductDTO product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/EditProductView.fxml"));
            loader.setControllerFactory(
                    param -> new EditProductView(commandBus, sessionManager, this::showInventoryDashboard));
            Parent editView = loader.load();

            EditProductView controller = loader.getController();
            controller.setProduct(product);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(editView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
