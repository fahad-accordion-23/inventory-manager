package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ledge.api.security.dto.RoleResponseDTO;
import ledge.api.security.dto.request.RegisterRoleRequestDTO;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.ui.clients.HttpSecurityClient;
import ledge.ui.core.SessionManager;

import java.util.*;

/**
 * UI for creating or editing roles.
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

    private RoleResponseDTO roleToEdit;
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

    public void setRole(RoleResponseDTO role) {
        this.roleToEdit = role;
        if (role != null) {
            roleNameField.setText(role.name());
            
            // Set checkboxes based on existing permissions
            role.permissions().forEach((resource, actions) -> {
                Map<Action, CheckBox> actionsMap = checkboxMap.get(resource);
                if (actionsMap != null) {
                    for (Action action : actions) {
                        CheckBox cb = actionsMap.get(action);
                        if (cb != null) {
                            cb.setSelected(true);
                        }
                    }
                }
            });
        }
    }

    private void setupMatrix() {
        Action[] actions = Action.values();
        for (int i = 0; i < actions.length; i++) {
            Label label = new Label(actions[i].name());
            label.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
            permissionMatrix.add(label, i + 1, 0);
        }

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
        String name = roleNameField.getText();
        if (name == null || name.isBlank()) {
            showAlert("Validation Error", "Role name is required.");
            return;
        }

        Map<Resource, List<Action>> permissions = new HashMap<>();
        checkboxMap.forEach((resource, actionMap) -> {
            List<Action> selectedActions = new ArrayList<>();
            actionMap.forEach((action, cb) -> {
                if (cb.isSelected()) {
                    selectedActions.add(action);
                }
            });
            if (!selectedActions.isEmpty()) {
                permissions.put(resource, selectedActions);
            }
        });

        RegisterRoleRequestDTO request = new RegisterRoleRequestDTO(name, permissions);

        sessionManager.getAuthContext().ifPresent(ctx -> {
            var response = (roleToEdit == null) 
                ? securityClient.registerRole(ctx, request)
                : securityClient.updateRole(ctx, roleToEdit.id(), request);

            if (response.success()) {
                onBack.run();
            } else {
                showAlert("Error", response.error().message());
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        onBack.run();
    }
}
