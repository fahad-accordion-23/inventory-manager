package ledge.users.writemodel.commands;

import ledge.shared.infrastructure.commands.Command;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;

import java.util.Optional;
import java.util.UUID;

public record ChangeUsernameCommand(UUID id, String newUsername) implements Command {
    private static final Permission REQUIRED = new Permission(Resource.USER, Action.UPDATE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
