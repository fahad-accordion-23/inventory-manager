package ledge.security.internal.application;

import ledge.security.api.IUserRoleService;
import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.exceptions.AuthorizationException;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.security.internal.domain.services.ISessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    private AuthorizationService authorizationService;
    private ISessionService sessionService;
    private IUserRoleService userRoleService;

    @BeforeEach
    void setUp() {
        sessionService = mock(ISessionService.class);
        userRoleService = mock(IUserRoleService.class);
        authorizationService = new AuthorizationService(sessionService, userRoleService);
    }

    @Test
    void testRequire_NullToken() {
        PermissionDTO perm = new PermissionDTO(Resource.PRODUCT, Action.CREATE);
        assertThrows(IllegalArgumentException.class, () -> {
            authorizationService.require(null, perm);
        });
    }

    @Test
    void testRequire_InvalidToken() {
        when(sessionService.getUserIdByToken("invalid-token")).thenReturn(Optional.empty());
        PermissionDTO perm = new PermissionDTO(Resource.PRODUCT, Action.CREATE);
        assertThrows(AuthorizationException.class, () -> {
            authorizationService.require("invalid-token", perm);
        });
    }

    @Test
    void testRequire_ValidTokenWithPermission() {
        UUID userId = UUID.randomUUID();
        String token = "valid-token";
        PermissionDTO perm = new PermissionDTO(Resource.PRODUCT, Action.CREATE);

        when(sessionService.getUserIdByToken(token)).thenReturn(Optional.of(userId));
        when(userRoleService.hasPermission(userId, perm)).thenReturn(true);

        assertDoesNotThrow(() -> {
            authorizationService.require(token, perm);
        });
    }

    @Test
    void testRequire_ValidTokenWithoutPermission() {
        UUID userId = UUID.randomUUID();
        String token = "valid-token";
        PermissionDTO perm = new PermissionDTO(Resource.USER, Action.CREATE);

        when(sessionService.getUserIdByToken(token)).thenReturn(Optional.of(userId));
        when(userRoleService.hasPermission(userId, perm)).thenReturn(false);

        assertThrows(AuthorizationException.class, () -> {
            authorizationService.require(token, perm);
        });
    }
}
