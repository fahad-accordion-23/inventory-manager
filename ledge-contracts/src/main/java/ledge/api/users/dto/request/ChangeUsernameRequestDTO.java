package ledge.api.users.dto.request;

import java.util.UUID;

public record ChangeUsernameRequestDTO(
        UUID user_id,
        String new_username) {}
