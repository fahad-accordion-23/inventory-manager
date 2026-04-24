package ledge.users.writemodel.commands;

import ledge.security.internal.domain.models.Action;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Resource;
import ledge.security.internal.domain.models.Role;
import ledge.shared.infrastructure.commands.Command;

import java.util.Optional;
import java.util.UUID;

public record ChangeUserRoleCommand(UUID id, Role newRole) implements Command {
    private static final Permission REQUIRED = new Permission(Resource.USER, Action.UPDATE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
