package ledge.users.readmodel.contracts;

import ledge.shared.infrastructure.queries.Query;
import ledge.security.writemodel.domain.Action;
import ledge.security.writemodel.domain.Permission;
import ledge.security.writemodel.domain.Resource;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.List;
import java.util.Optional;

public record GetAllUsersQuery() implements Query<List<UserDTO>> {
    private static final Permission REQUIRED = new Permission(Resource.USER, Action.READ);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
