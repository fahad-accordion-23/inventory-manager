package ledge.users.writemodel.commands;

import ledge.shared.infrastructure.commands.Command;
import ledge.security.writemodel.domain.Action;
import ledge.security.writemodel.domain.Permission;
import ledge.security.writemodel.domain.Resource;
import ledge.security.writemodel.domain.Role;
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