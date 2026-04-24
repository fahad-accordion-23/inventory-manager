package ledge.users.writemodel.commands;

import ledge.shared.infrastructure.commands.Command;
import ledge.security.writemodel.domain.Action;
import ledge.security.writemodel.domain.Permission;
import ledge.security.writemodel.domain.Resource;

import java.util.Optional;
import java.util.UUID;

public record RemoveUserCommand(UUID id) implements Command {
    private static final Permission REQUIRED = new Permission(Resource.USER, Action.DELETE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
