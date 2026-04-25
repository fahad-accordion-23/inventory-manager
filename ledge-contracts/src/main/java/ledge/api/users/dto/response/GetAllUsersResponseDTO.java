package ledge.api.users.dto.response;

import java.util.List;

import ledge.api.users.dto.UserResponseDTO;

public record GetAllUsersResponseDTO(List<UserResponseDTO> users) {

}
