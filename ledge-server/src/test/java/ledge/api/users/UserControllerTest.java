package ledge.api.users;

import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.api.users.dto.request.CreateUserRequestDTO;
import ledge.api.users.dto.response.UserResponseDTO;
import ledge.shared.types.Role;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.messaging.UserQueryBus;
import ledge.users.writemodel.infrastructure.messaging.UserCommandBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController controller;
    private UserCommandBus commandBus;
    private UserQueryBus queryBus;
    private AuthContext authContext;

    @BeforeEach
    void setUp() {
        commandBus = mock(UserCommandBus.class);
        queryBus = mock(UserQueryBus.class);
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
