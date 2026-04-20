package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ledge.shared.types.Role;
import ledge.ui.core.SessionManager;
import ledge.ui.util.FormValidator;
import ledge.users.application.commands.AddUserCommand;
import ledge.users.infrastructure.messaging.UserCommandBus;

public class AddUserView {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<Role> roleComboBox;

    private final UserCommandBus commandBus;
    private final SessionManager sessionManager;
    private final Runnable onCancel;

    public AddUserView(UserCommandBus commandBus, SessionManager sessionManager, Runnable onCancel) {
        this.commandBus = commandBus;
        this.sessionManager = sessionManager;
        this.onCancel = onCancel;
    }

    @FXML
    public void initialize() {
        roleComboBox.getItems().setAll(Role.values());
        roleComboBox.getSelectionModel().select(Role.SALES_STAFF);
    }

    @FXML
    public void handleRegister() {
        FormValidator v = new FormValidator();

        String username = v.requireNonBlank(usernameField, "Username");
        String password = v.requireNonBlank(passwordField, "Password");
        Role role = roleComboBox.getValue();

        if (v.hasErrors()) {
            new Alert(Alert.AlertType.ERROR, v.getErrorSummary()).showAndWait();
            return;
        }

        try {
            String token = sessionManager.getAuthToken().orElse("");
            commandBus.dispatch(new AddUserCommand(username, password, role), token);
            
            new Alert(Alert.AlertType.INFORMATION, "User registered successfully!").showAndWait();
            onCancel.run(); // Return to dashboard
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void handleCancel() {
        onCancel.run();
    }
}
