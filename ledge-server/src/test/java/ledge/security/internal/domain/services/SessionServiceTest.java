package ledge.security.internal.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {

    private ISessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService();
    }

    @Test
    void testCreateToken() {
        UUID userId = UUID.randomUUID();
        String token = sessionService.createToken(userId);

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
    void testGetUserIdByToken_ValidToken() {
        UUID userId = UUID.randomUUID();
        String token = sessionService.createToken(userId);

        Optional<UUID> fetched = sessionService.getUserIdByToken(token);
        assertTrue(fetched.isPresent());
        assertEquals(userId, fetched.get());
    }

    @Test
    void testGetUserIdByToken_InvalidToken() {
        Optional<UUID> fetched = sessionService.getUserIdByToken("invalid-token");
        assertFalse(fetched.isPresent());
    }

    @Test
    void testRemoveToken() {
        UUID userId = UUID.randomUUID();
        String token = sessionService.createToken(userId);

        assertTrue(sessionService.getUserIdByToken(token).isPresent());

        sessionService.removeToken(token);

        assertFalse(sessionService.getUserIdByToken(token).isPresent());
    }
}
