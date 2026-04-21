package ledge.api.users.dto.response;

import java.util.List;

/**
 * response DTO containing a list of users.
 */
public record UserListResponseDTO(List<UserResponseDTO> users) {}
