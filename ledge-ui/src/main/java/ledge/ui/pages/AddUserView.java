package ledge.ui.pages;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;

import ledge.api.shared.AuthContext;
import ledge.api.security.dto.RoleResponseDTO;
import ledge.api.security.dto.response.GetAllRolesResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.users.dto.UserResponseDTO;
import ledge.ui.clients.HttpSecurityClient;
import ledge.ui.clients.HttpUserClient;
import ledge.api.users.dto.request.CreateUserRequestDTO;
import ledge.ui.core.SessionManager;
import ledge.ui.util.FormValidator;

/**
 * Controller for creating a new user.
 * Aligned with the latest API response structures.
 */
public class AddUserView {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<RoleResponseDTO> roleComboBox;

    private final HttpUserClient userController;
    private final HttpSecurityClient securityController;
    private final SessionManager sessionManager;
    private final Runnable onCancel;

    public AddUserView(HttpUserClient userController, HttpSecurityClient securityController,
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
            public String toString(RoleResponseDTO role) {
                return role == null ? "" : role.name();
            }

            @Override
            public RoleResponseDTO fromString(String string) {
                return null;
            }
        });

        loadAvailableRoles();
    }

    private void loadAvailableRoles() {
        sessionManager.getAuthContext().ifPresent(context -> {
            ApiResponse<GetAllRolesResponseDTO> response = securityController.getAllRoles(context);
            if (response.success()) {
                roleComboBox.getItems().setAll(response.data().roles());
                // Select first role by default (visual only)
                if (!roleComboBox.getItems().isEmpty()) {
                    roleComboBox.getSelectionModel().selectFirst();
                }
            }
        });
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

        AuthContext authContext = sessionManager.getAuthContext().orElse(null);
        if (authContext == null) {
            new Alert(AlertType.ERROR, "You are not logged in.").showAndWait();
            return;
        }

        // NOTE: Selected role is ignored for now as the backend assigns DEFAULT_USER by default
        CreateUserRequestDTO request = new CreateUserRequestDTO(username, password);
        ApiResponse<UserResponseDTO> response = userController.createUser(authContext, request);

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
