package ledge.users.writemodel.commands;

import ledge.security.api.dto.PermissionDTO;
import ledge.shared.infrastructure.commands.Command;

import java.util.Optional;
import java.util.UUID;

public record RemoveUserCommand(UUID id) implements Command {

    private static final PermissionDTO REQUIRED = new PermissionDTO("USER", "DELETE");

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
