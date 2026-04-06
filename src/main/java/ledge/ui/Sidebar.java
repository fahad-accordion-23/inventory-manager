package ledge.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ledge.application.InventoryEventBroker;
import ledge.domain.Action;
import ledge.domain.Resource;
import ledge.domain.User;
import ledge.security.SecurityContext;
import ledge.security.event.LogoutRequestedEvent;

public class Sidebar {

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button addProductBtn;

    private final InventoryEventBroker eventBroker;
    private Runnable onShowDashboard;
    private Runnable onShowAddProduct;

    public Sidebar(InventoryEventBroker eventBroker) {
        this.eventBroker = eventBroker;
    }

    public void setCallbacks(Runnable onShowDashboard, Runnable onShowAddProduct) {
        this.onShowDashboard = onShowDashboard;
        this.onShowAddProduct = onShowAddProduct;
    }

    @FXML
    public void initialize() {
        applyRoleVisibility();
    }

    private void applyRoleVisibility() {
        if (!SecurityContext.isAuthenticated()) {
            return;
        }
        User user = SecurityContext.getCurrentUser();
        
        dashboardBtn.setVisible(user.getRole().hasPermission(Resource.PRODUCT, Action.READ));
        dashboardBtn.setManaged(dashboardBtn.isVisible());

        addProductBtn.setVisible(user.getRole().hasPermission(Resource.PRODUCT, Action.CREATE));
        addProductBtn.setManaged(addProductBtn.isVisible());
    }

    @FXML
    public void showInventoryDashboard() {
        if (onShowDashboard != null) {
            onShowDashboard.run();
        }
    }

    @FXML
    public void showAddProduct() {
        if (onShowAddProduct != null) {
            onShowAddProduct.run();
        }
    }

    @FXML
    public void handleLogout() {
        eventBroker.publish(new LogoutRequestedEvent());
    }

    public boolean isDashboardVisible() {
        return dashboardBtn.isVisible();
    }

    public boolean isAddProductVisible() {
        return addProductBtn.isVisible();
    }
}
