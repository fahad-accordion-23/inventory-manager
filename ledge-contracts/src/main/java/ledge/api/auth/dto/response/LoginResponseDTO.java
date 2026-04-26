package ledge.api.auth.dto.response;

import ledge.api.users.dto.UserResponseDTO;

/**
 * response DTO for a successful login request.
 */
public record LoginResponseDTO(
        String token,
        UserResponseDTO user) {
}
