package ledge.users.readmodel.contracts;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;
import ledge.shared.infrastructure.queries.Query;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.List;
import java.util.Optional;

public record GetAllUsersQuery() implements Query<List<UserDTO>> {
    private static final PermissionDTO REQUIRED = new PermissionDTO(Resource.USER, Action.READ);

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
