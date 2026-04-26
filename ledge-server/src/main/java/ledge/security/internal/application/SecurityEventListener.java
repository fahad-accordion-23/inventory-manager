package ledge.security.internal.application;

import ledge.security.api.IRoleService;
import ledge.security.api.IUserRoleService;
import ledge.security.api.dto.RoleDTO;
import ledge.users.events.integration.UserDeletedIntegrationEvent;
import ledge.users.events.integration.UserRegisteredIntegrationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener in the Security context that reacts to User module events.
 */
@Component
public class SecurityEventListener {
    private final IUserRoleService userRoleService;
    private final IRoleService roleService;

    public SecurityEventListener(IUserRoleService userRoleService, IRoleService roleService) {
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

    /**
     * Assigns the default role to a newly registered user.
     */
    @EventListener
    public void onUserRegistered(UserRegisteredIntegrationEvent event) {
        roleService.getRoleByName("DEFAULT_USER")
                .map(RoleDTO::id)
                .ifPresent(roleId -> userRoleService.assignRole(event.id(), roleId));
    }

    /**
     * Cleans up role assignments when a user is deleted.
     */
    @EventListener
    public void onUserDeleted(UserDeletedIntegrationEvent event) {
        userRoleService.removeRole(event.id());
    }
}
