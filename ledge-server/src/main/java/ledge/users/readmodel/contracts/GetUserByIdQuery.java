package ledge.users.readmodel.contracts;

import ledge.security.api.dto.PermissionDTO;
import ledge.shared.infrastructure.queries.Query;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.Optional;
import java.util.UUID;

public record GetUserByIdQuery(UUID id) implements Query<Optional<UserDTO>> {
    private static final PermissionDTO REQUIRED = new PermissionDTO("USER", "READ");

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
