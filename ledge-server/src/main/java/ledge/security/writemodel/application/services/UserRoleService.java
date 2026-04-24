package ledge.security.writemodel.application.services;

import ledge.security.shared.infrastructure.IRoleRepository;
import ledge.security.shared.infrastructure.IUserRoleRepository;
import ledge.security.writemodel.domain.Permission;

import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service that manages the mapping between user IDs and their assigned roles
 * using ID references.
 */
@Service
public class UserRoleService implements IUserRoleService {
    private final IUserRoleRepository userRoleRepository;
    private final IRoleRepository roleRepository;

    public UserRoleService(IUserRoleRepository userRoleRepository, IRoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void assignRole(UUID userId, UUID roleId) {
        if (userId == null || roleId == null) {
            throw new IllegalArgumentException("User ID and role ID cannot be null");
        }
        Set<UUID> roleIds = new HashSet<>(userRoleRepository.findRolesByUserId(userId));
        roleIds.add(roleId);
        userRoleRepository.saveRoles(userId, roleIds);
    }

    @Override
    public void removeRole(UUID userId, UUID roleId) {
        if (userId == null || roleId == null) {
            throw new IllegalArgumentException("User ID and role ID cannot be null");
        }
        Set<UUID> roleIds = new HashSet<>(userRoleRepository.findRolesByUserId(userId));
        if (roleIds.remove(roleId)) {
            userRoleRepository.saveRoles(userId, roleIds);
        }
    }

    @Override
    public Set<UUID> getRoleIds(UUID userId) {
        return Collections.unmodifiableSet(userRoleRepository.findRolesByUserId(userId));
    }

    @Override
    public boolean hasPermission(UUID userId, Permission permission) {
        return getRoleIds(userId).stream()
                .map(roleRepository::findById)
                .flatMap(Optional::stream)
                .anyMatch(role -> role.hasPermission(permission));
    }
}
