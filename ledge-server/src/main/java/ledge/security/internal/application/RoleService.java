package ledge.security.internal.application;

import ledge.security.api.IRoleService;
import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.dto.RoleDTO;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Role;
import ledge.security.internal.infrastructure.IRoleRepository;
import ledge.security.internal.infrastructure.IUserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing roles.
 */
@Service
public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;
    private final IUserRoleRepository userRoleRepository;

    public RoleService(IRoleRepository roleRepository, IUserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UUID registerRole(String name, Set<PermissionDTO> permissionDTOs) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }

        if (roleRepository.findByName(name).isPresent()) {
            throw new IllegalStateException("Role with name '" + name + "' already exists");
        }

        Set<Permission> permissions = mapPermissions(permissionDTOs);

        Role newRole = Role.register(name, permissions);
        roleRepository.save(newRole);
        return newRole.getId();
    }

    @Override
    public void updateRole(UUID roleId, String name, Set<PermissionDTO> permissionDTOs) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("Role not found with ID: " + roleId));

        // If name changes, check for unique name constraint
        if (name != null && !name.equalsIgnoreCase(role.getName())) {
            if (roleRepository.findByName(name).isPresent()) {
                throw new IllegalStateException("Role with name '" + name + "' already exists");
            }
        }

        Set<Permission> permissions = mapPermissions(permissionDTOs);
        role.update(name, permissions);
        roleRepository.save(role);
    }

    private Set<Permission> mapPermissions(Set<PermissionDTO> dtos) {
        Set<Permission> permissions = new HashSet<>();
        if (dtos != null) {
            for (PermissionDTO dto : dtos) {
                permissions.add(new Permission(dto.resource(), dto.action()));
            }
        }
        return permissions;
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
    public void deleteRole(UUID roleId) {
        // Check if role is used by any user
        if (userRoleRepository.isRoleAssigned(roleId)) {
            throw new IllegalStateException("Cannot delete role that is currently assigned to users");
        }
        
        roleRepository.delete(roleId);
    }

    private RoleDTO mapToDTO(Role role) {
        Set<PermissionDTO> permissions = role.getPermissions().stream()
                .map(p -> new PermissionDTO(p.resource(), p.action()))
                .collect(Collectors.toSet());
        return new RoleDTO(role.getId(), role.getName(), permissions);
    }
}
