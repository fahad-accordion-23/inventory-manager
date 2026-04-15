package ledge.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import ledge.application.InventoryCommandBus;
import ledge.application.InventoryEventBroker;
import ledge.application.InventoryQueryBus;
import ledge.application.command.AddProductCommand;
import ledge.application.dto.ProductDTO;
import ledge.application.query.GetAllProductsQuery;

import java.io.IOException;
import java.util.List;

public class MainLayout {

    @FXML
    private VBox contentArea;

    @FXML
    private Sidebar sidebarController;

    private final InventoryEventBroker eventBroker;
    private final InventoryCommandBus commandBus;
    private final InventoryQueryBus queryBus;
    
    private Parent dashboardViewCache;
    private Parent addProductViewCache;

    public MainLayout(InventoryEventBroker eventBroker, InventoryCommandBus commandBus, InventoryQueryBus queryBus) {
        this.eventBroker = eventBroker;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
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
                loader.setControllerFactory(param -> new AddProductView(commandBus));
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
                loader.setControllerFactory(param -> new InventoryDashboard(eventBroker, commandBus, queryBus, this::showEditProduct));
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
            loader.setControllerFactory(param -> new EditProductView(commandBus, this::showInventoryDashboard));
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
