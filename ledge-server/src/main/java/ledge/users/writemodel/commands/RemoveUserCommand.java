package ledge.users.writemodel.commands;

import ledge.security.internal.domain.models.Action;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Resource;
import ledge.shared.infrastructure.commands.Command;

import java.util.Optional;
import java.util.UUID;

public record RemoveUserCommand(UUID id) implements Command {
    private static final Permission REQUIRED = new Permission(Resource.USER, Action.DELETE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
