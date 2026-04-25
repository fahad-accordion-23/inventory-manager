package ledge.users.readmodel.contracts;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;
import ledge.shared.infrastructure.queries.Query;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.Optional;
import java.util.UUID;

public record GetUserByIdQuery(UUID id) implements Query<Optional<UserDTO>> {
    private static final PermissionDTO REQUIRED = new PermissionDTO(Resource.USER, Action.READ);

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.empty();

        // TODO: Add permission check
        // Returning empty for now to allow login initialization.
    }
}
