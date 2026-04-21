package ledge.security.application.services;

import ledge.security.application.events.AuthorizationException;
import ledge.security.domain.ISessionService;
import ledge.security.domain.SessionService;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;
import ledge.shared.types.Role;
import ledge.users.readmodel.dtos.UserDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationServiceTest {

    private IAuthorizationService authorizationService;
    private ISessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService();
        authorizationService = new AuthorizationService(sessionService);
    }

    @Test
    void testRequire_NullToken() {
        Permission perm = new Permission(Resource.PRODUCT, Action.CREATE);
        assertThrows(IllegalArgumentException.class, () -> {
            authorizationService.require(null, perm);
        });
    }

    @Test
    void testRequire_EmptyToken() {
        Permission perm = new Permission(Resource.PRODUCT, Action.CREATE);
        assertThrows(IllegalArgumentException.class, () -> {
            authorizationService.require("   ", perm);
        });
    }

    @Test
    void testRequire_InvalidToken() {
        Permission perm = new Permission(Resource.PRODUCT, Action.CREATE);
        assertThrows(AuthorizationException.class, () -> {
            authorizationService.require("invalid-token", perm);
        });
    }

    @Test
    void testRequire_ValidTokenWithPermission() {
        // ADMIN has all permissions
        UserDTO user = new UserDTO(UUID.randomUUID(), "testUser", "hash", Role.ADMIN);
        String token = sessionService.createToken(user);

        Permission perm = new Permission(Resource.PRODUCT, Action.CREATE);

        assertDoesNotThrow(() -> {
            authorizationService.require(token, perm);
        });
    }

    @Test
    void testRequire_ValidTokenWithoutPermission() {
        // SALES_STAFF does not have USER CREATE permission
        UserDTO user = new UserDTO(UUID.randomUUID(), "sales", "hash", Role.SALES_STAFF);
        String token = sessionService.createToken(user);

        Permission perm = new Permission(Resource.USER, Action.CREATE);

        assertThrows(AuthorizationException.class, () -> {
            authorizationService.require(token, perm);
        });
    }
}
