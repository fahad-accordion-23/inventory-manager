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
import ledge.ui.pages.AddProductView;
import ledge.ui.pages.EditProductView;
import ledge.ui.pages.InventoryDashboard;
import ledge.ui.pages.UserDashboardView;
import ledge.ui.pages.AddUserView;
import ledge.ui.pages.EditUserView;
import ledge.users.application.dtos.UserDTO;
import ledge.users.infrastructure.messaging.UserCommandBus;
import ledge.users.infrastructure.messaging.UserEventBroker;
import ledge.users.infrastructure.messaging.UserQueryBus;

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
    private final InventoryCommandBus inventoryCommandBus;
    private final InventoryQueryBus inventoryQueryBus;
    private final UserEventBroker userEventBroker;
    private final UserCommandBus userCommandBus;
    private final UserQueryBus userQueryBus;
    private final SessionManager sessionManager;
    private final UIEventBroker uiEventBroker;

    private Parent dashboardViewCache;
    private Parent addProductViewCache;
    private Parent userDashboardViewCache;
    private Parent addUserViewCache;

    public MainLayout(InventoryEventBroker inventoryEventBroker,
            InventoryCommandBus inventoryCommandBus,
            InventoryQueryBus inventoryQueryBus,
            UserEventBroker userEventBroker,
            UserCommandBus userCommandBus,
            UserQueryBus userQueryBus,
            SessionManager sessionManager,
            UIEventBroker uiEventBroker) {
        this.inventoryEventBroker = inventoryEventBroker;
        this.inventoryCommandBus = inventoryCommandBus;
        this.inventoryQueryBus = inventoryQueryBus;
        this.userEventBroker = userEventBroker;
        this.userCommandBus = userCommandBus;
        this.userQueryBus = userQueryBus;
        this.sessionManager = sessionManager;
        this.uiEventBroker = uiEventBroker;
    }

    @FXML
    public void initialize() {
        sidebarController.clearNavItems();
        sidebarController.addNavItem("Inventory Dashboard", Capability.VIEW_DASHBOARD, this::showInventoryDashboard);
        sidebarController.addNavItem("Add Product", Capability.CREATE_PRODUCT, this::showAddProduct);
        sidebarController.addNavItem("User Management", Capability.VIEW_USERS, this::showUserManagement);
    }

    @FXML
    public void showAddProduct() {
        if (addProductViewCache == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/AddProductView.fxml"));
                loader.setControllerFactory(param -> new AddProductView(inventoryCommandBus, sessionManager));
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
                        param -> new InventoryDashboard(inventoryEventBroker, inventoryCommandBus, inventoryQueryBus, sessionManager,
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

    @FXML
    public void showUserManagement() {
        if (userDashboardViewCache == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/UserDashboardView.fxml"));
                loader.setControllerFactory(param -> new UserDashboardView(userEventBroker, userCommandBus, userQueryBus, sessionManager,
                        this::showAddUser, this::showEditUser));
                userDashboardViewCache = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(userDashboardViewCache);
    }

    @FXML
    public void showAddUser() {
        if (addUserViewCache == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/AddUserView.fxml"));
                loader.setControllerFactory(param -> new AddUserView(userCommandBus, sessionManager, this::showUserManagement));
                addUserViewCache = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(addUserViewCache);
    }

    public void showEditUser(UserDTO user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/EditUserView.fxml"));
            loader.setControllerFactory(param -> new EditUserView(userCommandBus, sessionManager, this::showUserManagement));
            Parent editView = loader.load();

            EditUserView controller = loader.getController();
            controller.setUser(user);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(editView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEditProduct(ProductDTO product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/EditProductView.fxml"));
            loader.setControllerFactory(
                    param -> new EditProductView(inventoryCommandBus, sessionManager, this::showInventoryDashboard));
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
