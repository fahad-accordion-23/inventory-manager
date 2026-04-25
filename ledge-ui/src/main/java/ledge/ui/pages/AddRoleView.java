package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.ui.clients.HttpSecurityClient;
import ledge.ui.core.SessionManager;

import java.util.*;

/**
 * Dumb UI for creating or editing roles.
 * Displays a matrix of Resource x Actions.
 */
public class AddRoleView {
    @FXML
    private TextField roleNameField;
    @FXML
    private GridPane permissionMatrix;

    private final HttpSecurityClient securityClient;
    private final SessionManager sessionManager;
    private final Runnable onBack;

    private final Map<Resource, Map<Action, CheckBox>> checkboxMap = new HashMap<>();

    public AddRoleView(HttpSecurityClient securityClient, SessionManager sessionManager, Runnable onBack) {
        this.securityClient = securityClient;
        this.sessionManager = sessionManager;
        this.onBack = onBack;
    }

    @FXML
    public void initialize() {
        setupMatrix();
    }

    private void setupMatrix() {
        // Add headers for Actions
        Action[] actions = Action.values();
        for (int i = 0; i < actions.length; i++) {
            Label label = new Label(actions[i].name());
            label.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
            permissionMatrix.add(label, i + 1, 0);
        }

        // Add rows for Resources
        Resource[] resources = Resource.values();
        for (int j = 0; j < resources.length; j++) {
            Resource resource = resources[j];
            Label label = new Label(resource.name());
            label.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
            permissionMatrix.add(label, 0, j + 1);

            Map<Action, CheckBox> actionCheckboxes = new HashMap<>();
            for (int i = 0; i < actions.length; i++) {
                Action action = actions[i];
                CheckBox cb = new CheckBox();
                permissionMatrix.add(cb, i + 1, j + 1);
                actionCheckboxes.put(action, cb);
            }
            checkboxMap.put(resource, actionCheckboxes);
        }
    }

    @FXML
    private void handleSave() {
        // Functionality not added as per user request
    }

    @FXML
    private void handleCancel() {
        onBack.run();
    }
}
