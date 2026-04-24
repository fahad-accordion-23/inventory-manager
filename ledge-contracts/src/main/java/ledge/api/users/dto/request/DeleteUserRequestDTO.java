package ledge.api.users.dto.request;

import java.util.UUID;

public record DeleteUserRequestDTO(
        UUID user_id) {}
