package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import ledge.api.shared.AuthContext;
import ledge.api.shared.ApiResponse;
import ledge.ui.clients.HttpUserClient;
import ledge.api.users.dto.request.CreateUserRequestDTO;
import ledge.ui.core.SessionManager;
import ledge.ui.util.FormValidator;

public class AddUserView {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final HttpUserClient userController;
    private final SessionManager sessionManager;
    private final Runnable onCancel;

    public AddUserView(HttpUserClient userController, SessionManager sessionManager, Runnable onCancel) {
        this.userController = userController;
        this.sessionManager = sessionManager;
        this.onCancel = onCancel;
    }

    @FXML
    public void handleRegister() {
        FormValidator v = new FormValidator();

        String username = v.requireNonBlank(usernameField, "Username");
        String password = v.requireNonBlank(passwordField, "Password");

        if (v.hasErrors()) {
            new Alert(Alert.AlertType.ERROR, v.getErrorSummary()).showAndWait();
            return;
        }

        // CreateUserRequestDTO no longer includes the role; it's assigned by default in the handler
        CreateUserRequestDTO request = new CreateUserRequestDTO(username, password);

        AuthContext authContext = sessionManager.getAuthContext().orElse(null);
        if (authContext == null) {
            new Alert(AlertType.ERROR, "You are not logged in.").showAndWait();
            return;
        }

        ApiResponse<Void> response = userController.createUser(authContext, request);

        if (response.success()) {
            new Alert(AlertType.INFORMATION, "User registered successfully!").showAndWait();
            onCancel.run();
        } else {
            new Alert(AlertType.ERROR, response.error().message()).showAndWait();
        }
    }

    @FXML
    public void handleCancel() {
        onCancel.run();
    }
}
