package ledge.users.readmodel.contracts;

import ledge.shared.infrastructure.queries.Query;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.Optional;
import java.util.UUID;

public record GetUserByIdQuery(UUID id) implements Query<Optional<UserDTO>> {

    private static final Permission REQUIRED = new Permission(Resource.USER, Action.READ);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
