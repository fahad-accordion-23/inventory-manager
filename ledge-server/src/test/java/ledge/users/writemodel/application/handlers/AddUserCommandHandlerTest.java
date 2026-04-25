package ledge.users.writemodel.application.handlers;

import ledge.security.api.IRoleService;
import ledge.security.api.IUserRoleService;
import ledge.security.api.dto.RoleDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.writemodel.commands.AddUserCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddUserCommandHandlerTest {

    private AddUserCommandHandler handler;
    private IUserWriteRepository writeRepository;
    private IUserReadRepository readRepository;
    private IUserRoleService userRoleService;
    private IRoleService roleService;

    @BeforeEach
    void setUp() {
        writeRepository = mock(IUserWriteRepository.class);
        readRepository = mock(IUserReadRepository.class);
        userRoleService = mock(IUserRoleService.class);
        roleService = mock(IRoleService.class);
        handler = new AddUserCommandHandler(writeRepository, readRepository, userRoleService, roleService);
    }

    @Test
    void testHandleAddUser() {
        AddUserCommand command = new AddUserCommand("newuser", "password123");
        UUID roleId = UUID.randomUUID();
        RoleDTO defaultRole = new RoleDTO(roleId, "DEFAULT_USER", Set.of());
        
        when(roleService.getRoleByName("DEFAULT_USER")).thenReturn(Optional.of(defaultRole));

        handler.handle(command);

        // Verify write repository interaction
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(writeRepository).save(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();
        assertEquals("newuser", savedUser.getUsername());

        // Verify role assignment
        verify(userRoleService).assignRole(eq(savedUser.getId()), eq(roleId));

        // Verify read repository synchronization
        verify(readRepository).save(any());
    }
}
