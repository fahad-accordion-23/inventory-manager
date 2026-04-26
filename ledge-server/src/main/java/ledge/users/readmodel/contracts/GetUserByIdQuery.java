package ledge.users.readmodel.contracts;

import ledge.security.api.annotations.RequiresPermission;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.shared.infrastructure.queries.Query;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.Optional;
import java.util.UUID;

@RequiresPermission(resource = Resource.USER, action = Action.READ)
public record GetUserByIdQuery(UUID id) implements Query<Optional<UserDTO>> {
}
