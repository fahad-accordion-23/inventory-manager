package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ledge.shared.types.Role;
import ledge.ui.core.SessionManager;
import ledge.ui.util.FormValidator;
import ledge.users.application.commands.ChangeUserRoleCommand;
import ledge.users.application.commands.ChangeUsernameCommand;
import ledge.users.application.dtos.UserDTO;
import ledge.users.infrastructure.messaging.UserCommandBus;

import java.util.UUID;

/**
 * Controller for editing an existing user's details.
 */
public class EditUserView {

    @FXML
    private TextField usernameField;

    @FXML
    private ComboBox<Role> roleComboBox;

    private final UserCommandBus commandBus;
    private final SessionManager sessionManager;
    private final Runnable onCancel;
    private UUID userId;
    private String originalUsername;
    private Role originalRole;

    public EditUserView(UserCommandBus commandBus, SessionManager sessionManager, Runnable onCancel) {
        this.commandBus = commandBus;
        this.sessionManager = sessionManager;
        this.onCancel = onCancel;
    }

    @FXML
    public void initialize() {
        roleComboBox.getItems().setAll(Role.values());
    }

    public void setUser(UserDTO user) {
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

        try {
            String token = sessionManager.getAuthToken().orElse("");
            
            // Dispatch commands only if values changed
            if (!newUsername.equals(originalUsername)) {
                commandBus.dispatch(new ChangeUsernameCommand(userId, newUsername), token);
            }
            if (!newRole.equals(originalRole)) {
                commandBus.dispatch(new ChangeUserRoleCommand(userId, newRole), token);
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
