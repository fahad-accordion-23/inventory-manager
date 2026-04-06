package ledge.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ledge.application.InventoryEventBroker;
import ledge.domain.Action;
import ledge.domain.Resource;
import ledge.domain.User;
import ledge.security.SecurityContext;
import ledge.security.event.LogoutRequestedEvent;

import java.io.IOException;

public class MainLayout {

    @FXML
    private VBox contentArea;
    
    @FXML
    private Button dashboardBtn;
    
    @FXML
    private Button addProductBtn;
    
    private final InventoryEventBroker eventBroker;

    public MainLayout(InventoryEventBroker eventBroker) {
        this.eventBroker = eventBroker;
    }

    @FXML
    public void initialize() {
        applyRoleVisibility();
        // Load the initial view based on visibility
        if (dashboardBtn.isVisible()) {
            showInventoryDashboard();
        } else if (addProductBtn.isVisible()) {
            showAddProduct();
        }
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
    public void handleLogout() {
        eventBroker.publish(new LogoutRequestedEvent());
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
