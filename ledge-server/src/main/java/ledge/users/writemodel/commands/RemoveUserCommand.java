package ledge.users.writemodel.commands;

import ledge.security.api.dto.PermissionDTO;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.shared.infrastructure.commands.Command;

import java.util.Optional;
import java.util.UUID;

public record RemoveUserCommand(UUID id) implements Command {

    private static final PermissionDTO REQUIRED = new PermissionDTO(Resource.USER, Action.DELETE);

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
