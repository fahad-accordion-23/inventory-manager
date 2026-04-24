package ledge.shared.infrastructure.commands;

import ledge.security.api.IAuthorizationService;
import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.exceptions.AuthorizationException;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
    }

    @Test
    void testAuthorizationEnforcement() {
        TestHandlerWithPermission handler = new TestHandlerWithPermission();
        CommandBus commandBus = new CommandBus(List.of(handler), authService);

        PermissionDTO required = new PermissionDTO(Resource.PRODUCT, Action.UPDATE);
        doThrow(new AuthorizationException("Denied")).when(authService).require("bad-token", required);

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
        TestCommand(String data) { this.data = data; }
        @Override public Optional<PermissionDTO> getRequiredPermission() { return Optional.empty(); }
    }

    static class TestCommandWithPermission implements Command {
        @Override public Optional<PermissionDTO> getRequiredPermission() { 
            return Optional.of(new PermissionDTO(Resource.PRODUCT, Action.UPDATE)); 
        }
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
