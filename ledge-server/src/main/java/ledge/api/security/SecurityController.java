package ledge.api.security;

import ledge.api.security.dto.*;
import ledge.api.security.dto.request.AssignRoleRequestDTO;
import ledge.api.security.dto.request.RegisterRoleRequestDTO;
import ledge.api.security.dto.response.GetAllRolesResponseDTO;
import ledge.api.security.dto.response.GetUserRoleResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.ContractMapper;
import ledge.security.api.IRoleService;
import ledge.security.api.IUserRoleService;
import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.IAuthorizationService;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for managing security roles and assignments.
 * Aligned with the latest API documentation and split service layer.
 */
@RestController
@RequestMapping("/api/security")
public class SecurityController {
    private final IUserRoleService userRoleService;
    private final IRoleService roleService;
    private final IAuthorizationService authorizationService;

    public SecurityController(IUserRoleService userRoleService, IRoleService roleService, IAuthorizationService authorizationService) {
        this.userRoleService = userRoleService;
        this.roleService = roleService;
        this.authorizationService = authorizationService;
    }

    private String extractToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    /**
     * Lists all roles defined in the system.
     */
    @GetMapping("/roles")
    public ApiResponse<GetAllRolesResponseDTO> getAllRoles(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.READ));

        List<RoleResponseDTO> roleContracts = roleService.getAllRoles().stream()
                .map(ContractMapper::mapRole)
                .collect(Collectors.toList());

        return ApiResponse.success(new GetAllRolesResponseDTO(roleContracts));
    }

    /**
     * Retrieves the role ID assigned to a specific user.
     */
    @GetMapping("/users/{userId}/role")
    public ApiResponse<GetUserRoleResponseDTO> getAssignment(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("userId") UUID userId) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.READ));
        return ApiResponse.success(new GetUserRoleResponseDTO(userRoleService.getRoleId(userId).orElse(null)));
    }

    /**
     * Assigns a role to a user (overwrites existing).
     */
    @PutMapping("/users/{userId}/role")
    public ApiResponse<Void> assignRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("userId") UUID userId,
            @RequestBody AssignRoleRequestDTO request) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.UPDATE));
        userRoleService.assignRole(userId, request.roleId());
        return ApiResponse.success(null);
    }

    /**
     * Revokes the role assignment from the user.
     */
    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> removeRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("userId") UUID userId) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.DELETE));
        userRoleService.removeRole(userId);
        return ApiResponse.success(null);
    }

    /**
     * Registers a new custom role.
     */
    @PostMapping("/roles")
    public ApiResponse<Void> registerRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody RegisterRoleRequestDTO request) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.CREATE));

        Set<PermissionDTO> permissions = mapToInternal(request.permissions());
        roleService.registerRole(request.name(), permissions);
        return ApiResponse.success(null);
    }

    /**
     * Updates an existing role.
     */
    @PutMapping("/roles/{roleId}")
    public ApiResponse<Void> updateRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("roleId") UUID roleId,
            @RequestBody RegisterRoleRequestDTO request) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.UPDATE));

        Set<PermissionDTO> permissions = mapToInternal(request.permissions());
        roleService.updateRole(roleId, request.name(), permissions);
        return ApiResponse.success(null);
    }

    private Set<PermissionDTO> mapToInternal(java.util.Map<Resource, List<Action>> requestPermissions) {
        return requestPermissions.entrySet().stream()
                .flatMap(e -> e.getValue().stream().map(a -> new PermissionDTO(e.getKey(), a)))
                .collect(Collectors.toSet());
    }

    /**
     * Deletes a role from the system.
     */
    @DeleteMapping("/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("roleId") UUID roleId) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.DELETE));
        roleService.deleteRole(roleId);
        return ApiResponse.success(null);
    }
}
