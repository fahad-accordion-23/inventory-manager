package ledge.inventory.writemodel.contracts;

import ledge.security.api.dto.PermissionDTO;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.shared.infrastructure.commands.Command;
import java.util.Optional;
import java.util.UUID;

public record RemoveProductCommand(UUID productId) implements Command {

    private static final PermissionDTO REQUIRED = new PermissionDTO(Resource.PRODUCT, Action.DELETE);

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
