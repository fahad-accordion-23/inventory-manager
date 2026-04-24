package ledge.users.writemodel.application.handlers;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.writemodel.commands.ChangeUsernameCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;

public class ChangeUsernameCommandHandler {
    private final IUserWriteRepository userWriteRepository;
    private final IUserReadRepository userReadRepository;

    public ChangeUsernameCommandHandler(IUserWriteRepository userWriteRepository,
            IUserReadRepository userReadRepository) {
        this.userWriteRepository = userWriteRepository;
        this.userReadRepository = userReadRepository;
    }

    @CommandHandler
    public void handle(ChangeUsernameCommand command) throws IllegalArgumentException {
        User user = userWriteRepository.findById(command.id()).get();

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        User updatedUser = User.rehydrate(
                command.id(),
                command.newUsername(),
                user.getPasswordHash(),
                user.getRole());
        userWriteRepository.save(updatedUser);

        userReadRepository.save(UserDTO.fromUser(updatedUser));
    }
}
