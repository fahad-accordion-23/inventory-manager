package ledge.users.writemodel.application.handlers;

import org.springframework.stereotype.Service;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.writemodel.commands.ChangeUserRoleCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;

@Service
public class ChangeUserRoleCommandHandler implements CommandHandler<ChangeUserRoleCommand> {
    private final IUserWriteRepository userWriteRepository;
    private final IUserReadRepository userReadRepository;

    public ChangeUserRoleCommandHandler(IUserWriteRepository userWriteRepository,
            IUserReadRepository userReadRepository) {
        this.userWriteRepository = userWriteRepository;
        this.userReadRepository = userReadRepository;
    }

    @Override
    public void handle(ChangeUserRoleCommand command) {
        User user = userWriteRepository.findById(command.id()).get();

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        User updatedUser = User.rehydrate(
                command.id(),
                user.getUsername(),
                user.getPasswordHash(),
                command.newRole());
        userWriteRepository.save(updatedUser);

        userReadRepository.save(UserDTO.fromUser(updatedUser));
    };
}
