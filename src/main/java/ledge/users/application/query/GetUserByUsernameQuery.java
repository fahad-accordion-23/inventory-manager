package ledge.users.application.query;

import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;
import ledge.users.application.dtos.UserDTO;
import ledge.util.cqrs.Query;

import java.util.Optional;

public record GetUserByUsernameQuery(String username) implements Query<Optional<UserDTO>> {

    private static final Permission REQUIRED = new Permission(Resource.USER, Action.READ);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
