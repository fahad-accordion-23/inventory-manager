package ledge.security.app;

import ledge.inventory.app.InventoryEventBroker;
import ledge.security.app.event.AuthenticationException;
import ledge.security.app.event.LoginFailedEvent;
import ledge.security.app.event.LoginSucceededEvent;
import ledge.security.app.event.UserLoggedOutEvent;
import ledge.security.app.command.LoginCommand;
import ledge.security.app.command.LogoutCommand;
import ledge.security.domain.User;
import ledge.util.cqrs.CommandHandler;

public class AuthController {
    private final AuthService authService;
    private final InventoryEventBroker eventBroker;

    public AuthController(AuthService authService, InventoryEventBroker eventBroker) {
        this.authService = authService;
        this.eventBroker = eventBroker;
        this.eventBroker.register(this);
    }

    @CommandHandler
    private void handleLogin(LoginCommand event) {
        try {
            User user = authService.login(event.getUsername(), event.getPassword());
            eventBroker.publish(new LoginSucceededEvent(user));
        } catch (AuthenticationException e) {
            eventBroker.publish(new LoginFailedEvent(e.getMessage()));
        }
    }

    @CommandHandler
    private void handleLogout(LogoutCommand event) {
        authService.logout();
        eventBroker.publish(new UserLoggedOutEvent());
    }
}
