package ledge.security.internal.application;

import ledge.security.api.exceptions.AuthenticationException;
import ledge.security.internal.domain.services.ISessionService;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.util.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private AuthenticationService authService;
    private ISessionService sessionService;
    private IUserReadRepository userReadRepository;

    @BeforeEach
    void setUp() {
        sessionService = mock(ISessionService.class);
        userReadRepository = mock(IUserReadRepository.class);
        authService = new AuthenticationService(sessionService, userReadRepository);
    }

    @Test
    void testSuccessfulLogin() throws AuthenticationException {
        String username = "testuser";
        String password = "password123";
        String hashedPassword = PasswordHasher.hash(password);
        UUID userId = UUID.randomUUID();
        
        UserDTO user = new UserDTO(userId, username, hashedPassword);
        
        when(userReadRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(sessionService.createToken(userId)).thenReturn("mock-token");

        String token = authService.login(username, password);

        assertEquals("mock-token", token);
        verify(sessionService).createToken(userId);
    }

    @Test
    void testFailedLogin_InvalidUser() {
        when(userReadRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authService.login("none", "pass"));
    }

    @Test
    void testFailedLogin_WrongPassword() {
        String username = "testuser";
        String hashedPassword = PasswordHasher.hash("correct-pass");
        UserDTO user = new UserDTO(UUID.randomUUID(), username, hashedPassword);

        when(userReadRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class, () -> authService.login(username, "wrong-pass"));
    }

    @Test
    void testLogout() {
        authService.logout("valid-token");
        verify(sessionService).removeToken("valid-token");
    }
}
