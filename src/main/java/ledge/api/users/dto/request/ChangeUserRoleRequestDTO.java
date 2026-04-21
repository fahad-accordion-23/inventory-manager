package ledge.api.users.dto.request;

import ledge.shared.types.Role;
import java.util.UUID;

public record ChangeUserRoleRequestDTO(
        UUID user_id,
        Role new_role) {}
