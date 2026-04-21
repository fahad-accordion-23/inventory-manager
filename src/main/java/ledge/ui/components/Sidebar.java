package ledge.ui.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import ledge.ui.core.Capability;
import ledge.ui.core.SessionManager;
import ledge.ui.events.UserLoggedOutEvent;
import ledge.ui.messaging.UIEventBroker;

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

    private boolean firstNav = true;

    public void clearNavItems() {
        navArea.getChildren().clear();
        firstNav = true;
    }

    /**
     * Builds nav buttons from the provided items, showing only those the current
     * user's role has permission for via the SessionManager capabilities.
     */
    public void addNavItem(String label, Capability capability, Runnable action) {
        if (!sessionManager.isAuthenticated()) {
            return;
        }

        if (sessionManager.isAllowed(capability)) {
            Button btn = new Button(label);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle(
                    "-fx-padding: 10 15; -fx-background-color: transparent; -fx-text-fill: #2d3436; -fx-alignment: center-left; -fx-font-size: 14; -fx-cursor: hand;");
            btn.setOnMouseEntered(_ -> btn.setStyle(
                    "-fx-padding: 10 15; -fx-background-color: #e9ecef; -fx-text-fill: #2d3436; -fx-alignment: center-left; -fx-font-size: 14; -fx-cursor: hand;"));
            btn.setOnMouseExited(_ -> btn.setStyle(
                    "-fx-padding: 10 15; -fx-background-color: transparent; -fx-text-fill: #2d3436; -fx-alignment: center-left; -fx-font-size: 14; -fx-cursor: hand;"));
            btn.setOnAction(_ -> action.run());
            navArea.getChildren().add(btn);

            // Auto-navigate to the first accessible item
            if (firstNav) {
                action.run();
                firstNav = false;
            }
        }
    }

    @FXML
    public void handleLogout() {
        sessionManager.logout();
        uiEventBroker.publish(new UserLoggedOutEvent());
    }
}
