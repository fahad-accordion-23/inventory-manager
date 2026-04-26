package ledge.api.security.dto.response;

import java.util.List;

import ledge.api.security.dto.RoleResponseDTO;

public record GetAllRolesResponseDTO(List<RoleResponseDTO> roles) {
}
