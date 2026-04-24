package ledge.api.users.dto.request;

import java.util.UUID;

public record ChangePasswordRequestDTO(
        UUID user_id,
        String new_password) {}
