package ledge.api.users;

import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.api.users.dto.request.CreateUserRequestDTO;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.shared.types.Role;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.shared.infrastructure.queries.QueryBus;
import ledge.shared.infrastructure.commands.CommandBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController controller;
    private CommandBus commandBus;
    private QueryBus queryBus;
    private AuthContext authContext;

    @BeforeEach
    void setUp() {
        commandBus = mock(CommandBus.class);
        queryBus = mock(QueryBus.class);
        controller = new UserController(commandBus, queryBus);
        authContext = new AuthContext("token");
    }

    @Test
    void testGetAllUsers() {
        UUID id = UUID.randomUUID();
        UserDTO user = new UserDTO(id, "targetUser", "hash", Role.SALES_STAFF);
        when(queryBus.dispatch(any(), eq("token"))).thenReturn(List.of(user));

        ApiResponse<?> response = controller.getAllUsers("Bearer " + authContext.token());

        assertTrue(response.success());
    }

    @Test
    void testCreateUser() {
        CreateUserRequestDTO request = new CreateUserRequestDTO("newuser", "password", Role.SALES_STAFF);

        ApiResponse<UserResponseDTO> response = controller.createUser("Bearer " + authContext.token(), request);

        assertTrue(response.success());
        verify(commandBus).dispatch(any(), eq("token"));
    }
}
