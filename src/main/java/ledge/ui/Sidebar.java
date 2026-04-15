package ledge.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ledge.application.InventoryCommandBus;
import ledge.application.InventoryEventBroker;
import ledge.security.Role;
import ledge.security.SecurityContext;
import ledge.security.command.LogoutCommand;

import java.util.List;

public class Sidebar {

    @FXML
    private VBox navArea;

    private final InventoryEventBroker eventBroker;
    private final InventoryCommandBus commandBus;

    public Sidebar(InventoryEventBroker eventBroker, InventoryCommandBus commandBus) {
        this.eventBroker = eventBroker;
        this.commandBus = commandBus;
    }

    @FXML
    public void initialize() {
        // Nav items are provided later via setNavItems() — nothing to do here yet.
    }

    /**
     * Builds nav buttons from the provided items, showing only those the current
     * user's role has permission for. The permission check uses each NavItem's own
     * REQUIRED constant — no (Resource, Action) pairs hardcoded here.
     */
    public void setNavItems(List<NavItem> items) {
        navArea.getChildren().clear();

        if (!SecurityContext.isAuthenticated()) {
            return;
        }

        Role role = SecurityContext.getCurrentUser().getRole();
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
        commandBus.dispatch(new LogoutCommand());
    }
}
