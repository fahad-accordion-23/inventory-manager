package ledge.ui.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ledge.security.domain.Role;
import ledge.security.domain.User;
import ledge.ui.SessionManager;
import ledge.ui.events.UserLoggedOutEvent;
import ledge.ui.messaging.UIEventBroker;

import java.util.List;

public class Sidebar {

    @FXML
    private VBox navArea;

    private final SessionManager sessionManager;
    private final UIEventBroker uiEventBroker;

    public Sidebar(SessionManager sessionManager, UIEventBroker uiEventBroker) {
        this.sessionManager = sessionManager;
        this.uiEventBroker = uiEventBroker;
    }

    @FXML
    public void initialize() {
        // Nav items are provided later via setNavItems()
    }

    /**
     * Builds nav buttons from the provided items, showing only those the current
     * user's role has permission for.
     */
    public void setNavItems(List<NavItem> items) {
        navArea.getChildren().clear();

        if (!sessionManager.isAuthenticated()) {
            return;
        }

        User user = sessionManager.getCurrentUser().orElse(null);
        if (user == null) {
            return;
        }

        Role role = user.getRole();
        boolean first = true;

        for (NavItem item : items) {
            if (role.hasPermission(item.requiredPermissions())) {
                Button btn = new Button(item.label());
                btn.setMaxWidth(Double.MAX_VALUE);
                btn.setOnAction(_ -> item.action().run());
                navArea.getChildren().add(btn);

                // Auto-navigate to the first accessible item
                if (first) {
                    item.action().run();
                    first = false;
                }
            }
        }
    }

    @FXML
    public void handleLogout() {
        sessionManager.logout();
        uiEventBroker.publish(new UserLoggedOutEvent());
    }
}
