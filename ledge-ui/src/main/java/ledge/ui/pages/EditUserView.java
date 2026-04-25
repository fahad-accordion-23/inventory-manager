package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.api.users.dto.request.ChangeUsernameRequestDTO;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.api.security.dto.AssignRoleRequestDTO;
import ledge.security.api.dto.RoleDTO;
import ledge.ui.clients.HttpSecurityClient;
import ledge.ui.clients.HttpUserClient;
import ledge.ui.core.SessionManager;
import ledge.ui.util.FormValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for editing an existing user's details.
 * Interacts with both User and Security clients.
 */
public class EditUserView {

    @FXML
    private TextField usernameField;

    @FXML
    private ComboBox<RoleDTO> roleComboBox;

    private final HttpUserClient userController;
    private final HttpSecurityClient securityController;
    private final SessionManager sessionManager;
    private final Runnable onCancel;

    private UUID userId;
    private String originalUsername;
    private RoleDTO originalRole;

    public EditUserView(HttpUserClient userController, HttpSecurityClient securityController,
            SessionManager sessionManager, Runnable onCancel) {
        this.userController = userController;
        this.securityController = securityController;
        this.sessionManager = sessionManager;
        this.onCancel = onCancel;
    }

    @FXML
    public void initialize() {
        roleComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(RoleDTO role) {
                return role == null ? "" : role.name();
            }

            @Override
            public RoleDTO fromString(String string) {
                return null;
            }
        });

        loadAvailableRoles();
    }

    private void loadAvailableRoles() {
        sessionManager.getAuthContext().ifPresent(context -> {
            ApiResponse<List<RoleDTO>> response = securityController.getAllRoles(context);
            if (response.success()) {
                roleComboBox.getItems().setAll(response.data());
                if (originalRole != null) {
                    roleComboBox.setValue(originalRole);
                }
            }
        });
    }

    /**
     * Pre-populate the form with an existing user's data.
     */
    public void setUser(UserResponseDTO user) {
        this.userId = user.id();
        this.originalUsername = user.username();
        this.originalRole = user.role();

        usernameField.setText(originalUsername);
        if (!roleComboBox.getItems().isEmpty()) {
            roleComboBox.setValue(originalRole);
        }
    }

    @FXML
    public void handleSave() {
        FormValidator v = new FormValidator();
        String newUsername = v.requireNonBlank(usernameField, "Username");
        RoleDTO newRole = roleComboBox.getValue();

        if (v.hasErrors()) {
            new Alert(Alert.AlertType.ERROR, v.getErrorSummary()).showAndWait();
            return;
        }

        Optional<AuthContext> authContext = sessionManager.getAuthContext();
        if (authContext.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "You are not logged in.").showAndWait();
            return;
        }

        AuthContext context = authContext.get();

        try {
            // Update username if changed
            if (!newUsername.equals(originalUsername)) {
                ApiResponse<Void> response = userController.changeUsername(context, userId,
                        new ChangeUsernameRequestDTO(newUsername));

                if (!response.success()) {
                    throw new Exception(response.error().message());
                }
            }

            // Update role if changed
            if (newRole != null && (originalRole == null || !newRole.id().equals(originalRole.id()))) {
                ApiResponse<Void> response = securityController.assignRole(context, userId,
                        new AssignRoleRequestDTO(newRole.id()));

                if (!response.success()) {
                    throw new Exception(response.error().message());
                }
            }

            new Alert(Alert.AlertType.INFORMATION, "User updated successfully!").showAndWait();
            onCancel.run();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Update failed: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void handleCancel() {
        onCancel.run();
    }
}
