package ledge.api.shared;

import ledge.api.security.dto.RoleResponseDTO;
import ledge.api.users.dto.UserResponseDTO;
import ledge.security.api.dto.RoleDTO;
import ledge.security.api.dto.PermissionDTO;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for mapping internal read models and DTOs to API contracts.
 */
public class ContractMapper {

    /**
     * Maps an internal UserDTO and its role to the API UserResponseDTO.
     */
    public static UserResponseDTO mapUser(UserDTO user, RoleDTO internalRole) {
        RoleResponseDTO roleContract = internalRole != null ? mapRole(internalRole) : null;
        return new UserResponseDTO(user.id(), user.username(), roleContract);
    }

    /**
     * Maps an internal RoleDTO to the API RoleResponseDTO.
     * Note: Current RoleResponseDTO contract uses a Map<Resource, Action>.
     * If multiple actions exist for a resource, the first one is preserved.
     */
    public static RoleResponseDTO mapRole(RoleDTO role) {
        Map<Resource, Action> permissionMap = role.permissions().stream()
                .collect(Collectors.toMap(
                        PermissionDTO::resource,
                        PermissionDTO::action,
                        (existing, replacement) -> existing
                ));
        return new RoleResponseDTO(role.id(), role.name(), permissionMap);
    }
}
