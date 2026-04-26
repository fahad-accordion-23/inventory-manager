package ledge.users.writemodel.commands;

import ledge.security.api.annotations.RequiresPermission;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.shared.infrastructure.commands.Command;

import java.util.UUID;

@RequiresPermission(resource = Resource.USER, action = Action.UPDATE)
public record ChangeUsernameCommand(UUID id, String newUsername) implements Command {
}
