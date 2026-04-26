package ledge.shared.infrastructure.commands;

import ledge.security.api.IAuthorizationService;
import ledge.security.api.exceptions.AuthorizationException;
import ledge.security.api.annotations.RequiresPermission;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandBusTest {

    private IAuthorizationService authService;

    @BeforeEach
    void setUp() {
        authService = mock(IAuthorizationService.class);
    }

    @Test
    void testDispatch() {
        TestHandler handler = new TestHandler();
        CommandBus commandBus = new CommandBus(List.of(handler), authService);

        TestCommand command = new TestCommand("data");
        commandBus.dispatch(command, "valid-token");

        assertTrue(handler.executed);
        assertEquals("data", handler.receivedData);
        verify(authService).authorize(eq("valid-token"), eq(command));
    }

    @Test
    void testAuthorizationEnforcement() {
        TestHandlerWithPermission handler = new TestHandlerWithPermission();
        CommandBus commandBus = new CommandBus(List.of(handler), authService);

        doThrow(new AuthorizationException("Denied")).when(authService).authorize(eq("bad-token"), any(Object.class));

        TestCommandWithPermission command = new TestCommandWithPermission();

        assertThrows(AuthorizationException.class, () -> commandBus.dispatch(command, "bad-token"));
        assertFalse(handler.executed);
    }

    @Test
    void testDuplicateHandlerRegistration() {
        TestHandler h1 = new TestHandler();
        TestHandler h2 = new TestHandler();
        assertThrows(IllegalStateException.class, () -> new CommandBus(List.of(h1, h2), authService));
    }

    @Test
    void testMissingHandler() {
        CommandBus commandBus = new CommandBus(List.of(), authService);
        assertThrows(IllegalStateException.class, () -> commandBus.dispatch(new TestCommand("data"), "token"));
    }

    // Test classes
    static class TestCommand implements Command {
        final String data;

        TestCommand(String data) {
            this.data = data;
        }
    }

    @RequiresPermission(resource = Resource.PRODUCT, action = Action.UPDATE)
    static class TestCommandWithPermission implements Command {
    }

    static class TestHandler implements CommandHandler<TestCommand> {
        boolean executed = false;
        String receivedData;

        @Override
        public void handle(TestCommand command) {
            this.executed = true;
            this.receivedData = command.data;
        }
    }

    static class TestHandlerWithPermission implements CommandHandler<TestCommandWithPermission> {
        boolean executed = false;

        @Override
        public void handle(TestCommandWithPermission command) {
            this.executed = true;
        }
    }
}
