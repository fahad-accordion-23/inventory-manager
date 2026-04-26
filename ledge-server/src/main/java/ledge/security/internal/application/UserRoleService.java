package ledge.security.internal.application;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.IUserRoleService;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.infrastructure.IRoleRepository;
import ledge.security.internal.infrastructure.IUserRoleRepository;

import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service that manages the mapping between user IDs and their assigned roles.
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
        userRoleRepository.saveRole(userId, roleId);
    }

    @Override
    public void removeRole(UUID userId) {
        userRoleRepository.deleteRole(userId);
    }

    @Override
    public Optional<UUID> getRoleId(UUID userId) {
        return userRoleRepository.findRoleByUserId(userId);
    }

    @Override
    public boolean hasPermission(UUID userId, PermissionDTO permissionDTO) {
        if (permissionDTO == null)
            return true;

        Permission permission = new Permission(permissionDTO.resource(), permissionDTO.action());

        return getRoleId(userId)
                .flatMap(roleRepository::findById)
                .map(role -> role.hasPermission(permission))
                .orElse(false);
    }
}
