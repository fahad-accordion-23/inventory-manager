package ledge.security.internal.application;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.dto.RoleDTO;
import ledge.security.api.IUserRoleService;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Role;
import ledge.security.internal.infrastructure.IRoleRepository;
import ledge.security.internal.infrastructure.IUserRoleRepository;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDTO> getRole(UUID roleId) {
        return roleRepository.findById(roleId).map(this::mapToDTO);
    }

    @Override
    public Optional<RoleDTO> getRoleByName(String name) {
        return roleRepository.findByName(name).map(this::mapToDTO);
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

    private RoleDTO mapToDTO(Role role) {
        Set<PermissionDTO> permissions = role.getPermissions().stream()
                .map(p -> new PermissionDTO(p.resource(), p.action()))
                .collect(Collectors.toSet());
        return new RoleDTO(role.getId(), role.getName(), permissions);
    }
}
