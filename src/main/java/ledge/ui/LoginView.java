package ledge.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ledge.application.InventoryEventBroker;
import ledge.security.event.LoginFailedEvent;
import ledge.security.event.LoginRequestedEvent;

public class LoginView {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final InventoryEventBroker eventBroker;

    public LoginView(InventoryEventBroker eventBroker) {
        this.eventBroker = eventBroker;
        this.eventBroker.subscribe(LoginFailedEvent.class, this::handleLoginFailed);
    }

    @FXML
    public void initialize() {
        // Initialization if needed
    }

    @FXML
    public void handleLogin() {
        errorLabel.setText("");
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password");
            return;
        }

        eventBroker.publish(new LoginRequestedEvent(username, password));
    }

    private void handleLoginFailed(LoginFailedEvent event) {
        Platform.runLater(() -> errorLabel.setText(event.getReason()));
    }
}
