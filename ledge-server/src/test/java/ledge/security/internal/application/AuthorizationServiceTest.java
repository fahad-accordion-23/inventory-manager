package ledge.security.internal.application;

import ledge.security.api.IPermissionResolver;
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
    private IPermissionResolver permissionResolver;

    @BeforeEach
    void setUp() {
        sessionService = mock(ISessionService.class);
        userRoleService = mock(IUserRoleService.class);
        permissionResolver = mock(IPermissionResolver.class);
        authorizationService = new AuthorizationService(sessionService, userRoleService, permissionResolver);
    }

    @Test
    void testAuthorize_NullToken() {
        PermissionDTO perm = new PermissionDTO(Resource.PRODUCT, Action.CREATE);
        assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(null, perm);
        });
    }

    @Test
    void testAuthorize_InvalidToken() {
        when(sessionService.getUserIdByToken("invalid-token")).thenReturn(Optional.empty());
        PermissionDTO perm = new PermissionDTO(Resource.PRODUCT, Action.CREATE);
        assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize("invalid-token", perm);
        });
    }

    @Test
    void testAuthorize_ValidTokenWithPermission() {
        UUID userId = UUID.randomUUID();
        String token = "valid-token";
        PermissionDTO perm = new PermissionDTO(Resource.PRODUCT, Action.CREATE);

        when(sessionService.getUserIdByToken(token)).thenReturn(Optional.of(userId));
        when(userRoleService.hasPermission(userId, perm)).thenReturn(true);

        assertDoesNotThrow(() -> {
            authorizationService.authorize(token, perm);
        });
    }

    @Test
    void testAuthorize_ValidTokenWithoutPermission() {
        UUID userId = UUID.randomUUID();
        String token = "valid-token";
        PermissionDTO perm = new PermissionDTO(Resource.USER, Action.CREATE);

        when(sessionService.getUserIdByToken(token)).thenReturn(Optional.of(userId));
        when(userRoleService.hasPermission(userId, perm)).thenReturn(false);

        assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(token, perm);
        });
    }

    @Test
    void testAuthorize_WithContext() {
        UUID userId = UUID.randomUUID();
        String token = "valid-token";
        Object context = new Object();
        PermissionDTO perm = new PermissionDTO(Resource.PRODUCT, Action.CREATE);

        when(sessionService.getUserIdByToken(token)).thenReturn(Optional.of(userId));
        when(permissionResolver.resolve(context)).thenReturn(Optional.of(perm));
        when(userRoleService.hasPermission(userId, perm)).thenReturn(true);

        assertDoesNotThrow(() -> {
            authorizationService.authorize(token, context);
        });

        verify(permissionResolver).resolve(context);
    }
}
