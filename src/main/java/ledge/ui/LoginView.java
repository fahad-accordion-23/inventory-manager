package ledge.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ledge.application.InventoryCommandBus;
import ledge.application.InventoryEventBroker;
import ledge.security.command.LoginCommand;
import ledge.security.event.LoginFailedEvent;
import ledge.util.event.Subscribe;

public class LoginView {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final InventoryEventBroker eventBroker;
    private final InventoryCommandBus commandBus;

    public LoginView(InventoryEventBroker eventBroker, InventoryCommandBus commandBus) {
        this.eventBroker = eventBroker;
        this.commandBus = commandBus;
        this.eventBroker.register(this);
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

        commandBus.dispatch(new LoginCommand(username, password));
    }

    @Subscribe
    private void handleLoginFailed(LoginFailedEvent event) {
        Platform.runLater(() -> errorLabel.setText(event.getReason()));
    }
}
