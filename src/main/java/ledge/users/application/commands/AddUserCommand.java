package ledge.users.application.commands;

import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;
import ledge.shared.types.Role;
import ledge.util.cqrs.Command;

import java.util.Optional;

public record AddUserCommand(String username, String password, Role role) implements Command {

    private static final Permission REQUIRED = new Permission(Resource.USER, Action.CREATE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}