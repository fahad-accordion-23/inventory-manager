package ledge.api.security;

import ledge.api.security.dto.AssignRoleRequestDTO;
import ledge.api.security.dto.RegisterRoleRequestDTO;
import ledge.api.shared.ApiResponse;
import ledge.security.api.dto.RoleDTO;
import ledge.security.api.IUserRoleService;
import ledge.security.api.IAuthorizationService;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;
import ledge.security.api.dto.PermissionDTO;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing security roles and assignments.
 */
@RestController
@RequestMapping("/api/security")
public class SecurityController {
    private final IUserRoleService userRoleService;
    private final IAuthorizationService authorizationService;

    public SecurityController(IUserRoleService userRoleService, IAuthorizationService authorizationService) {
        this.userRoleService = userRoleService;
        this.authorizationService = authorizationService;
    }

    private String extractToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    /**
     * Lists all available roles.
     */
    @GetMapping("/roles")
    public ApiResponse<List<RoleDTO>> getAllRoles(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.READ));
        return ApiResponse.success(userRoleService.getAllRoles());
    }

    /**
     * Registers a new custom role.
     */
    @PostMapping("/roles")
    public ApiResponse<UUID> registerRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody RegisterRoleRequestDTO request) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.CREATE));
        UUID roleId = userRoleService.registerRole(request.name(), request.permissions());
        return ApiResponse.success(roleId);
    }

    /**
     * Retrieves the role assigned to a specific user.
     */
    @GetMapping("/assignments/{userId}")
    public ApiResponse<UUID> getAssignment(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID userId) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.READ));
        return ApiResponse.success(userRoleService.getRoleId(userId).orElse(null));
    }

    /**
     * Assigns a role to a user (overwrites existing).
     */
    @PutMapping("/assignments/{userId}")
    public ApiResponse<Void> assignRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID userId,
            @RequestBody AssignRoleRequestDTO request) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.UPDATE));
        userRoleService.assignRole(userId, request.roleId());
        return ApiResponse.success(null);
    }

    /**
     * Revokes the role from a user.
     */
    @DeleteMapping("/assignments/{userId}")
    public ApiResponse<Void> removeRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID userId) {
        authorizationService.require(extractToken(authHeader), new PermissionDTO(Resource.ROLE, Action.DELETE));
        userRoleService.removeRole(userId);
        return ApiResponse.success(null);
    }
}
