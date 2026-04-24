package ledge.users.writemodel.commands;

import ledge.security.api.dto.PermissionDTO;
import ledge.shared.infrastructure.commands.Command;
import ledge.users.readmodel.dtos.UserDTO;

import java.util.Optional;

public record AddUserCommand(String username, String password) implements Command {

    private static final PermissionDTO REQUIRED = new PermissionDTO("USER", "CREATE");

    public AddUserCommand(UserDTO user) {
        this(user.username(), user.hashedPassword());
    }

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}