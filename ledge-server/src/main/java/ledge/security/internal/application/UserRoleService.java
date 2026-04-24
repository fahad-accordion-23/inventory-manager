package ledge.security.internal.application;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.IUserRoleService;
import ledge.security.internal.domain.models.Action;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Resource;
import ledge.security.internal.domain.models.Role;
import ledge.security.internal.infrastructure.IRoleRepository;
import ledge.security.internal.infrastructure.IUserRoleRepository;

import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service that manages the mapping between user IDs and their assigned roles
 * using ID references and allows registration of custom roles.
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
    public UUID registerRole(String name, Set<PermissionDTO> permissionDTOs) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }

        Set<Permission> permissions = new HashSet<>();
        if (permissionDTOs != null) {
            for (PermissionDTO dto : permissionDTOs) {
                permissions.add(new Permission(
                        Resource.valueOf(dto.resource().toUpperCase()),
                        Action.valueOf(dto.action().toUpperCase())));
            }
        }

        Role newRole = new Role(name, permissions);
        roleRepository.save(newRole);
        return newRole.getId();
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
    public boolean hasPermission(UUID userId, PermissionDTO permissionDTO) {
        if (permissionDTO == null)
            return true;

        try {
            Permission permission = new Permission(
                    Resource.valueOf(permissionDTO.resource().toUpperCase()),
                    Action.valueOf(permissionDTO.action().toUpperCase()));

            return getRoleIds(userId).stream()
                    .map(roleRepository::findById)
                    .flatMap(Optional::stream)
                    .anyMatch(role -> role.hasPermission(permission));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
