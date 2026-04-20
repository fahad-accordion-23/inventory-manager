package ledge.ui.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import ledge.inventory.application.commands.AddProductCommand;
import ledge.inventory.application.dtos.ProductDTO;
import ledge.inventory.application.query.GetAllProductsQuery;
import ledge.inventory.infrastructure.messaging.InventoryCommandBus;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.inventory.infrastructure.messaging.InventoryQueryBus;
import ledge.ui.SessionManager;
import ledge.ui.messaging.UIEventBroker;

import java.io.IOException;
import java.util.List;

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
        List<NavItem> navItems = List.of(
                new NavItem("Inventory Dashboard", GetAllProductsQuery.REQUIRED,
                        this::showInventoryDashboard),
                new NavItem("Add Product", AddProductCommand.REQUIRED, this::showAddProduct));

        sidebarController.setNavItems(navItems);
    }

    @FXML
    public void showAddProduct() {
        if (addProductViewCache == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/AddProductView.fxml"));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/InventoryDashboard.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/EditProductView.fxml"));
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
