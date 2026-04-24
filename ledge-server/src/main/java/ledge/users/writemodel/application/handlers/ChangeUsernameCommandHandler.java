package ledge.users.writemodel.application.handlers;

import org.springframework.stereotype.Service;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.writemodel.commands.ChangeUsernameCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;

@Service
public class ChangeUsernameCommandHandler implements CommandHandler<ChangeUsernameCommand> {
    private final IUserWriteRepository userWriteRepository;
    private final IUserReadRepository userReadRepository;

    public ChangeUsernameCommandHandler(IUserWriteRepository userWriteRepository,
            IUserReadRepository userReadRepository) {
        this.userWriteRepository = userWriteRepository;
        this.userReadRepository = userReadRepository;
    }

    @Override
    public void handle(ChangeUsernameCommand command) {
        User user = userWriteRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        User updatedUser = User.rehydrate(
                command.id(),
                command.newUsername(),
                user.getPasswordHash());
        userWriteRepository.save(updatedUser);

        userReadRepository.save(UserDTO.fromUser(updatedUser));
    }
}
