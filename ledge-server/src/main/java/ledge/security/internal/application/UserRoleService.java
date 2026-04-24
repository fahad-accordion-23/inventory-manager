package ledge.security.internal.application;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.IUserRoleService;
import ledge.security.internal.domain.models.Permission;
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
                permissions.add(new Permission(dto.resource(), dto.action()));
            }
        }

        Role newRole = Role.register(name, permissions);
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

        Permission permission = new Permission(permissionDTO.resource(), permissionDTO.action());

        return getRoleIds(userId).stream()
                .map(roleRepository::findById)
                .flatMap(Optional::stream)
                .anyMatch(role -> role.hasPermission(permission));
    }
}
