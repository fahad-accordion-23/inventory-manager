package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.api.users.UserController;
import ledge.api.users.dto.request.ChangeUserRoleRequestDTO;
import ledge.api.users.dto.request.ChangeUsernameRequestDTO;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.shared.types.Role;
import ledge.ui.core.SessionManager;
import ledge.ui.util.FormValidator;

import java.util.Optional;
import java.util.UUID;

/**
 * Controller for editing an existing user's details via the API layer.
 */
public class EditUserView {

    @FXML
    private TextField usernameField;

    @FXML
    private ComboBox<Role> roleComboBox;

    private final UserController userController;
    private final SessionManager sessionManager;
    private final Runnable onCancel;
    private UUID userId;
    private String originalUsername;
    private Role originalRole;

    public EditUserView(UserController userController, SessionManager sessionManager, Runnable onCancel) {
        this.userController = userController;
        this.sessionManager = sessionManager;
        this.onCancel = onCancel;
    }

    @FXML
    public void initialize() {
        roleComboBox.getItems().setAll(Role.values());
    }

    /**
     * Pre-populate the form with an existing user's data.
     */
    public void setUser(UserResponseDTO user) {
        this.userId = user.id();
        this.originalUsername = user.username();
        this.originalRole = user.role();

        usernameField.setText(originalUsername);
        roleComboBox.setValue(originalRole);
    }

    @FXML
    public void handleSave() {
        FormValidator v = new FormValidator();
        String newUsername = v.requireNonBlank(usernameField, "Username");
        Role newRole = roleComboBox.getValue();

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
            // Dispatch updates only if values changed
            if (!newUsername.equals(originalUsername)) {
                ApiResponse<Void> response = userController.changeUsername(context,
                        new ChangeUsernameRequestDTO(userId, newUsername));

                if (!response.success()) {
                    throw new Exception(response.error().message());
                }
            }
            if (!newRole.equals(originalRole)) {
                ApiResponse<Void> response = userController.changeRole(context,
                        new ChangeUserRoleRequestDTO(userId, newRole));

                if (!response.success()) {
                    throw new Exception(response.error().message());
                }
            }

            new Alert(Alert.AlertType.INFORMATION, "User updated successfully!").showAndWait();
            onCancel.run();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void handleCancel() {
        onCancel.run();
    }
}
