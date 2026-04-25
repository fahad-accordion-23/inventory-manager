package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        addRoleButton.setDisable(!sessionManager.isAllowed(Capability.MANAGE_ROLES));
        
        refresh();
    }

    private void refresh() {
        sessionManager.getAuthContext().ifPresent(ctx -> {
            var response = securityClient.getAllRoles(ctx);
            if (response.success()) {
                roleTable.getItems().setAll(response.data().roles());
            }
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
}
