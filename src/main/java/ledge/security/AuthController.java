package ledge.security;

import ledge.application.InventoryEventBroker;
import ledge.security.event.AuthenticationException;
import ledge.security.event.LoginFailedEvent;
import ledge.security.event.LoginSucceededEvent;
import ledge.security.event.UserLoggedOutEvent;
import ledge.security.command.LoginCommand;
import ledge.security.command.LogoutCommand;
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
