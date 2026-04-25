package ledge.ui.pages;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ledge.api.security.dto.RoleResponseDTO;
import ledge.ui.clients.HttpSecurityClient;
import ledge.ui.core.Capability;
import ledge.ui.core.SessionManager;

import java.util.function.Consumer;

/**
 * View for listing and managed system roles.
 */
public class RoleDashboardView {
    @FXML
    private TableView<RoleResponseDTO> roleTable;
    @FXML
    private TableColumn<RoleResponseDTO, String> nameColumn;
    @FXML
    private Button addRoleButton;

    private final HttpSecurityClient securityClient;
    private final SessionManager sessionManager;
    private final Runnable onAddRole;
    private final Consumer<RoleResponseDTO> onEditRole;

    public RoleDashboardView(HttpSecurityClient securityClient,
            SessionManager sessionManager,
            Runnable onAddRole,
            Consumer<RoleResponseDTO> onEditRole) {
        this.securityClient = securityClient;
        this.sessionManager = sessionManager;
        this.onAddRole = onAddRole;
        this.onEditRole = onEditRole;
    }

    @FXML
    public void initialize() {
        // Use lambda for record access since PropertyValueFactory expects Beans (getName)
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));

        addRoleButton.setDisable(!sessionManager.isAllowed(Capability.MANAGE_ROLES));

        refresh();
    }

    @FXML
    public void refresh() {
        sessionManager.getAuthContext().ifPresentOrElse(ctx -> {
            var response = securityClient.getAllRoles(ctx);
            if (response.success()) {
                roleTable.getItems().setAll(response.data().roles());
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to load roles: " + response.error().message()).showAndWait();
            }
        }, () -> {
            new Alert(Alert.AlertType.WARNING, "Not authenticated").showAndWait();
        });
    }

    @FXML
    private void handleAddRole() {
        onAddRole.run();
    }

    @FXML
    private void handleEditRole() {
        RoleResponseDTO selected = roleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            onEditRole.accept(selected);
        }
    }

    @FXML
    private void handleDeleteRole() {
        RoleResponseDTO selected = roleTable.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        sessionManager.getAuthContext().ifPresent(ctx -> {
            var response = securityClient.deleteRole(ctx, selected.id());
            if (response.success()) {
                refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Could not delete role: " + response.error().message());
                alert.showAndWait();
            }
        });
    }
}
