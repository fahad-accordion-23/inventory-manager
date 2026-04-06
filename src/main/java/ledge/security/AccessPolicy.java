package ledge.security;

import ledge.domain.Action;
import ledge.domain.Permission;
import ledge.domain.Resource;
import ledge.domain.User;
import ledge.security.event.AuthorizationException;

public final class AccessPolicy {
    private AccessPolicy() {
        // Utility class
    }

    public static void require(Permission permission) {
        if (!SecurityContext.isAuthenticated()) {
            throw new AuthorizationException("User is not authenticated");
        }

        User user = SecurityContext.getCurrentUser();
        if (!user.getRole().hasPermission(permission)) {
            throw new AuthorizationException(
                String.format("Role '%s' lacks permission for %s on %s",
                              user.getRole().getName(), permission.action(), permission.resource())
            );
        }
    }

    public static void require(Resource resource, Action action) {
        require(new Permission(resource, action));
    }
}
