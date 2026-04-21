package ledge.shared.infrastructure.commands;

import ledge.security.application.services.IAuthorizationService;
import ledge.security.application.events.AuthorizationException;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandBusTest {

    private CommandBus commandBus;
    private IAuthorizationService authService;

    @BeforeEach
    void setUp() {
        authService = mock(IAuthorizationService.class);
        commandBus = new CommandBus(authService);
    }

    @Test
    void testRegisterAndDispatch() {
        TestHandler handler = new TestHandler();
        commandBus.register(handler);

        TestCommand command = new TestCommand("data");
        commandBus.dispatch(command, "valid-token");

        assertTrue(handler.executed);
        assertEquals("data", handler.receivedData);
    }

    @Test
    void testAuthorizationEnforcement() {
        TestHandler handler = new TestHandler();
        commandBus.register(handler);

        doThrow(new AuthorizationException("Denied")).when(authService).require("bad-token", new Permission(Resource.PRODUCT, Action.UPDATE));

        TestCommandWithPermission command = new TestCommandWithPermission();
        
        assertThrows(AuthorizationException.class, () -> commandBus.dispatch(command, "bad-token"));
        assertFalse(handler.executed);
    }

    @Test
    void testDuplicateHandlerRegistration() {
        commandBus.register(new TestHandler());
        assertThrows(IllegalStateException.class, () -> commandBus.register(new TestHandler()));
    }

    @Test
    void testMissingHandler() {
        assertThrows(IllegalStateException.class, () -> commandBus.dispatch(new TestCommand("data"), "token"));
    }

    // Test classes
    static class TestCommand implements Command {
        final String data;
        TestCommand(String data) { this.data = data; }
        @Override public Optional<Permission> getRequiredPermission() { return Optional.empty(); }
    }

    static class TestCommandWithPermission implements Command {
        @Override public Optional<Permission> getRequiredPermission() { return Optional.of(new Permission(Resource.PRODUCT, Action.UPDATE)); }
    }

    static class TestHandler {
        boolean executed = false;
        String receivedData;

        @CommandHandler
        public void handle(TestCommand command) {
            this.executed = true;
            this.receivedData = command.data;
        }

        @CommandHandler
        public void handlePermissioned(TestCommandWithPermission command) {
            this.executed = true;
        }
    }
}
