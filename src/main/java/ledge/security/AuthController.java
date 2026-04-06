package ledge.security;

import ledge.application.InventoryEventBroker;
import ledge.domain.User;
import ledge.security.event.AuthenticationException;
import ledge.security.event.LoginFailedEvent;
import ledge.security.event.LoginRequestedEvent;
import ledge.security.event.LoginSucceededEvent;
import ledge.security.event.LogoutRequestedEvent;

public class AuthController {
    private final AuthService authService;
    private final InventoryEventBroker eventBroker;

    public AuthController(AuthService authService, InventoryEventBroker eventBroker) {
        this.authService = authService;
        this.eventBroker = eventBroker;

        this.eventBroker.subscribe(LoginRequestedEvent.class, this::handleLoginRequested);
        this.eventBroker.subscribe(LogoutRequestedEvent.class, this::handleLogoutRequested);
    }

    private void handleLoginRequested(LoginRequestedEvent event) {
        try {
            User user = authService.login(event.getUsername(), event.getPassword());
            eventBroker.publish(new LoginSucceededEvent(user));
        } catch (AuthenticationException e) {
            eventBroker.publish(new LoginFailedEvent(e.getMessage()));
        }
    }

    private void handleLogoutRequested(LogoutRequestedEvent event) {
        authService.logout();
    }
}
