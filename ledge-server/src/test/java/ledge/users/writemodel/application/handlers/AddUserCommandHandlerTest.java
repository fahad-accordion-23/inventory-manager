package ledge.users.writemodel.application.handlers;

import ledge.shared.types.Role;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.writemodel.commands.AddUserCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddUserCommandHandlerTest {

    private AddUserCommandHandler handler;
    private IUserWriteRepository writeRepository;
    private IUserReadRepository readRepository;

    @BeforeEach
    void setUp() {
        writeRepository = mock(IUserWriteRepository.class);
        readRepository = mock(IUserReadRepository.class);
        handler = new AddUserCommandHandler(writeRepository, readRepository);
    }

    @Test
    void testHandleAddUser() {
        AddUserCommand command = new AddUserCommand("newuser", "password123", Role.SALES_STAFF);
        
        handler.handle(command);

        // Verify write repository interaction
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(writeRepository).save(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();
        assertEquals("newuser", savedUser.getUsername());
        assertEquals(Role.SALES_STAFF, savedUser.getRole());
        assertNotEquals("password123", savedUser.getPasswordHash()); // Should be hashed

        // Verify read repository synchronization
        verify(readRepository).save(any());
    }
}
