package ledge.users.writemodel.application.handlers;

import org.springframework.stereotype.Service;

import ledge.security.api.IRoleService;
import ledge.security.api.IUserRoleService;
import ledge.security.api.dto.RoleDTO;
import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.writemodel.commands.AddUserCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import ledge.util.PasswordHasher;

@Service
public class AddUserCommandHandler implements CommandHandler<AddUserCommand> {
    private final IUserWriteRepository userWriteRepository;
    private final IUserReadRepository userReadRepository;
    private final IUserRoleService userRoleService;
    private final IRoleService roleService;

    public AddUserCommandHandler(IUserWriteRepository userWriteRepository,
            IUserReadRepository userReadRepository,
            IUserRoleService userRoleService,
            IRoleService roleService) {
        this.userWriteRepository = userWriteRepository;
        this.userReadRepository = userReadRepository;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

    @Override
    public void handle(AddUserCommand command) {
        String passwordHash = PasswordHasher.hash(command.password());
        User user = User.register(command.username(), passwordHash);
        userWriteRepository.save(user);

        // Assign default role (DEFAULT_USER)
        roleService.getRoleByName("DEFAULT_USER")
                .map(RoleDTO::id)
                .ifPresent(roleId -> userRoleService.assignRole(user.getId(), roleId));

        userReadRepository.save(UserDTO.fromUser(user));
    }
}
