package ledge.users.writemodel.commands;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;
import ledge.shared.infrastructure.commands.Command;

import java.util.Optional;
import java.util.UUID;

public record ChangeUserPasswordCommand(UUID id, String newPassword) implements Command {

    private static final PermissionDTO REQUIRED = new PermissionDTO(Resource.USER, Action.UPDATE);

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
