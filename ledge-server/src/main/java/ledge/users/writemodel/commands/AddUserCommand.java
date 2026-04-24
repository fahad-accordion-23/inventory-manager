package ledge.users.writemodel.commands;

import ledge.security.internal.domain.models.Action;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Resource;
import ledge.security.internal.domain.models.Role;
import ledge.shared.infrastructure.commands.Command;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.Optional;

public record AddUserCommand(String username, String password, Role role) implements Command {

    private static final Permission REQUIRED = new Permission(Resource.USER, Action.CREATE);

    public AddUserCommand(UserDTO user) {
        this(user.username(), user.hashedPassword(), user.role());
    }

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}