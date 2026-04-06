package ledge.security;

import ledge.domain.Action;
import ledge.domain.Resource;
import ledge.domain.User;
import ledge.security.event.AuthorizationException;

public final class AccessPolicy {
    private AccessPolicy() {
        // Utility class
    }

    public static void require(Resource resource, Action action) {
        if (!SecurityContext.isAuthenticated()) {
            throw new AuthorizationException("User is not authenticated");
        }

        User user = SecurityContext.getCurrentUser();
        if (!user.getRole().hasPermission(resource, action)) {
            throw new AuthorizationException(
                String.format("Role %s lacks permission for %s on %s", 
                              user.getRole().getName(), action, resource)
            );
        }
    }
}
