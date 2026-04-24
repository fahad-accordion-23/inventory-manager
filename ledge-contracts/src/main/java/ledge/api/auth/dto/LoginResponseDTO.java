package ledge.api.auth.dto;

import ledge.api.users.dto.response.UserResponseDTO;

/**
 * response DTO for a successful login request.
 */
public record LoginResponseDTO(
        String token,
        UserResponseDTO user) {}
