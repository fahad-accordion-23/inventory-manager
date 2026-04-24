package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import ledge.api.shared.AuthContext;
import ledge.api.shared.ApiResponse;
import ledge.api.users.UserController;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.api.users.dto.request.CreateUserRequestDTO;
import ledge.shared.types.Role;
import ledge.ui.core.SessionManager;
import ledge.ui.util.FormValidator;

public class AddUserView {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<Role> roleComboBox;

    private final UserController userController;
    private final SessionManager sessionManager;
    private final Runnable onCancel;

    public AddUserView(UserController userController, SessionManager sessionManager, Runnable onCancel) {
        this.userController = userController;
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

        CreateUserRequestDTO request = new CreateUserRequestDTO(username, password, role);

        AuthContext authContext = sessionManager.getAuthContext().orElse(null);
        if (authContext == null) {
            Alert alert = new Alert(AlertType.ERROR, "You are not logged in.");
            alert.showAndWait();
            return;
        }

        ApiResponse<UserResponseDTO> response = userController.createUser(authContext, request);

        if (response.success()) {
            Alert alert = new Alert(AlertType.INFORMATION, "User registered successfully!");
            alert.showAndWait();
            onCancel.run();
        } else {
            Alert alert = new Alert(AlertType.ERROR, response.error().message());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCancel() {
        onCancel.run();
    }
}
