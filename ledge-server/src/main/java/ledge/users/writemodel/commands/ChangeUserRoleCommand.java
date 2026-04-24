package ledge.users.writemodel.commands;

import ledge.shared.infrastructure.commands.Command;
import ledge.security.writemodel.domain.Action;
import ledge.security.writemodel.domain.Permission;
import ledge.security.writemodel.domain.Resource;
import ledge.security.writemodel.domain.Role;

import java.util.Optional;
import java.util.UUID;

public record ChangeUserRoleCommand(UUID id, Role newRole) implements Command {
    private static final Permission REQUIRED = new Permission(Resource.USER, Action.UPDATE);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
