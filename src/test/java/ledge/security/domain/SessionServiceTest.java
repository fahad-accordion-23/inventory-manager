package ledge.security.domain;

import ledge.shared.types.Role;
import ledge.users.application.dtos.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {

    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService();
    }

    @Test
    void testCreateToken() {
        UserDTO user = new UserDTO(UUID.randomUUID(), "testUser", Role.ADMIN);
        String token = sessionService.createToken(user);
        
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void testCreateToken_NullUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.createToken(null);
        });
    }

    @Test
    void testGetUserByToken_ValidToken() {
        UserDTO user = new UserDTO(UUID.randomUUID(), "testUser", Role.ADMIN);
        String token = sessionService.createToken(user);
        
        Optional<UserDTO> fetched = sessionService.getUserByToken(token);
        assertTrue(fetched.isPresent());
        assertEquals("testUser", fetched.get().username());
    }

    @Test
    void testGetUserByToken_InvalidToken() {
        Optional<UserDTO> fetched = sessionService.getUserByToken("invalid-token");
        assertFalse(fetched.isPresent());
    }

    @Test
    void testGetUserByToken_NullToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.getUserByToken(null);
        });
    }

    @Test
    void testRemoveToken() {
        UserDTO user = new UserDTO(UUID.randomUUID(), "testUser", Role.ADMIN);
        String token = sessionService.createToken(user);
        
        assertTrue(sessionService.getUserByToken(token).isPresent());
        
        sessionService.removeToken(token);
        
        assertFalse(sessionService.getUserByToken(token).isPresent());
    }

    @Test
    void testRemoveToken_NullToken() {
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.removeToken(null);
        });
    }
}
