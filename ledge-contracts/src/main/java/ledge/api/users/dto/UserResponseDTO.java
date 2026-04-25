package ledge.api.users.dto;

import java.util.UUID;

import ledge.api.security.dto.RoleResponseDTO;

public record UserResponseDTO(UUID id, String username, RoleResponseDTO role) {

}
