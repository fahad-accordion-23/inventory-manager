package ledge.users.writemodel.commands;

import ledge.shared.infrastructure.commands.Command;
import ledge.security.api.annotations.RequiresPermission;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.users.readmodel.dtos.UserDTO;

/**
 * Command to add a new user.
 */
@RequiresPermission(resource = Resource.USER, action = Action.CREATE)
public record AddUserCommand(String username, String password) implements Command {
    public AddUserCommand(UserDTO user) {
        this(user.username(), user.hashedPassword());
    }
}