package ledge.ui.pages;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import ledge.shared.infrastructure.events.Subscribe;
import ledge.ui.core.SessionManager;
import ledge.ui.events.LoginFailedEvent;
import ledge.ui.events.LoginSucceededEvent;
import ledge.ui.messaging.UIEventBroker;

/**
 * Controller for the Login view.
 * Coordinates with SessionManager to authenticate users.
 */
public class LoginView {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final SessionManager sessionManager;
    private final UIEventBroker uiEventBroker;

    public LoginView(SessionManager sessionManager, UIEventBroker uiEventBroker) {
        this.sessionManager = sessionManager;
        this.uiEventBroker = uiEventBroker;
        this.uiEventBroker.register(this);
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

        try {
            sessionManager.login(username, password);
            uiEventBroker.publish(new LoginSucceededEvent());
        } catch (Exception e) {
            uiEventBroker.publish(new LoginFailedEvent(e.getMessage()));
        }
    }

    @Subscribe
    private void handleLoginFailed(LoginFailedEvent event) {
        Platform.runLater(() -> errorLabel.setText(event.getReason()));
    }
}
