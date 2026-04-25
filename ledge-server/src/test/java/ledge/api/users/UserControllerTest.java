package ledge.api.users;

import ledge.api.shared.ApiResponse;
import ledge.api.users.dto.request.CreateUserRequestDTO;
import ledge.api.users.dto.response.GetAllUsersResponseDTO;
import ledge.api.users.dto.UserResponseDTO;
import ledge.security.api.IUserRoleService;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.shared.infrastructure.queries.QueryBus;
import ledge.shared.infrastructure.commands.CommandBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController controller;
    private CommandBus commandBus;
    private QueryBus queryBus;
    private IUserRoleService userRoleService;

    @BeforeEach
    void setUp() {
        commandBus = mock(CommandBus.class);
        queryBus = mock(QueryBus.class);
        userRoleService = mock(IUserRoleService.class);
        controller = new UserController(commandBus, queryBus, userRoleService);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetAllUsers() {
        UUID id = UUID.randomUUID();
        UserDTO user = new UserDTO(id, "targetUser", "hash");
        when(queryBus.dispatch(any(), any())).thenReturn(List.of(user));

        ApiResponse<GetAllUsersResponseDTO> response = controller.getAllUsers("Bearer token");

        assertTrue(response.success());
        assertEquals(1, response.data().users().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testCreateUser() {
        CreateUserRequestDTO request = new CreateUserRequestDTO("newuser", "password");
        UserDTO user = new UserDTO(UUID.randomUUID(), "newuser", "hash");
        
        when(queryBus.dispatch(any(), any())).thenReturn(Optional.of(user));

        ApiResponse<UserResponseDTO> response = controller.createUser("Bearer token", request);

        assertTrue(response.success());
        assertEquals("newuser", response.data().username());
        verify(commandBus).dispatch(any(), eq("token"));
    }
}
