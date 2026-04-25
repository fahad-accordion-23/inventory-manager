package ledge.ui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import ledge.ui.clients.HttpSecurityClient;
import ledge.ui.clients.HttpInventoryClient;
import ledge.api.inventory.dto.ProductResponseDTO;
import ledge.api.security.dto.RoleResponseDTO;
import ledge.ui.clients.HttpUserClient;
import ledge.api.users.dto.UserResponseDTO;
import ledge.ui.core.Capability;
import ledge.ui.core.SessionManager;
import ledge.ui.messaging.UIEventBroker;
import ledge.ui.pages.AddProductView;
import ledge.ui.pages.EditProductView;
import ledge.ui.pages.InventoryDashboard;
import ledge.ui.pages.UserDashboardView;
import ledge.ui.pages.AddUserView;
import ledge.ui.pages.EditUserView;
import ledge.ui.pages.RoleDashboardView;
import ledge.ui.pages.AddRoleView;

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

    private final HttpInventoryClient inventoryController;
    private final HttpUserClient userController;
    private final HttpSecurityClient securityController;
    private final SessionManager sessionManager;

    private Parent dashboardViewCache;
    private Parent addProductViewCache;
    private Parent userDashboardViewCache;
    private Parent addUserViewCache;
    private Parent roleDashboardViewCache;

    public MainLayout(HttpInventoryClient inventoryController,
            HttpUserClient userController,
            HttpSecurityClient securityController,
            SessionManager sessionManager,
            UIEventBroker uiEventBroker) {
        this.inventoryController = inventoryController;
        this.userController = userController;
        this.securityController = securityController;
        this.sessionManager = sessionManager;
    }

    @FXML
    public void initialize() {
        sidebarController.clearNavItems();
        sidebarController.addNavItem("Inventory Dashboard", Capability.VIEW_DASHBOARD, this::showInventoryDashboard);
        sidebarController.addNavItem("Add Product", Capability.CREATE_PRODUCT, this::showAddProduct);
        sidebarController.addNavItem("User Management", Capability.VIEW_USERS, this::showUserManagement);
        sidebarController.addNavItem("Role Management", Capability.VIEW_ROLES, this::showRoleManagement);
    }

    @FXML
    public void showAddProduct() {
        if (addProductViewCache == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/AddProductView.fxml"));
                loader.setControllerFactory(param -> new AddProductView(inventoryController, sessionManager));
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
                        param -> new InventoryDashboard(inventoryController, sessionManager,
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
                loader.setControllerFactory(
                        param -> new UserDashboardView(userController, sessionManager,
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
                loader.setControllerFactory(
                        param -> new AddUserView(userController, securityController, sessionManager, this::showUserManagement));
                addUserViewCache = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(addUserViewCache);
    }

    @FXML
    public void showRoleManagement() {
        if (roleDashboardViewCache == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/RoleDashboardView.fxml"));
                loader.setControllerFactory(
                        param -> new RoleDashboardView(securityController, sessionManager,
                                this::showAddRole, this::showEditRole));
                roleDashboardViewCache = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(roleDashboardViewCache);
    }

    @FXML
    public void showAddRole() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/AddRoleView.fxml"));
            loader.setControllerFactory(
                    param -> new AddRoleView(securityController, sessionManager, this::showRoleManagement));
            Parent addView = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(addView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEditRole(RoleResponseDTO role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/AddRoleView.fxml"));
            loader.setControllerFactory(
                    param -> new AddRoleView(securityController, sessionManager, this::showRoleManagement));
            Parent editView = loader.load();

            AddRoleView controller = loader.getController();
            controller.setRole(role);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(editView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEditUser(UserResponseDTO user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/EditUserView.fxml"));
            loader.setControllerFactory(
                    param -> new EditUserView(userController, securityController, sessionManager, this::showUserManagement));
            Parent editView = loader.load();

            EditUserView controller = loader.getController();
            controller.setUser(user);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(editView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEditProduct(ProductResponseDTO product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/EditProductView.fxml"));
            loader.setControllerFactory(
                    param -> new EditProductView(inventoryController, sessionManager, this::showInventoryDashboard));
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
