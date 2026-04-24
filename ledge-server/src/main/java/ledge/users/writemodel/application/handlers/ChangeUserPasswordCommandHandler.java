package ledge.users.writemodel.application.handlers;

import org.springframework.stereotype.Service;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.writemodel.commands.ChangeUserPasswordCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;

@Service
public class ChangeUserPasswordCommandHandler implements CommandHandler<ChangeUserPasswordCommand> {
    private final IUserWriteRepository userWriteRepository;
    private final IUserReadRepository userReadRepository;

    public ChangeUserPasswordCommandHandler(IUserWriteRepository userRepository,
            IUserReadRepository userReadRepository) {
        this.userWriteRepository = userRepository;
        this.userReadRepository = userReadRepository;
    }

    @Override
    public void handle(ChangeUserPasswordCommand command) {
        User user = userWriteRepository.findById(command.id()).get();

        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        User updatedUser = User.rehydrate(
                command.id(),
                user.getUsername(),
                command.newPassword(),
                user.getRole());
        userWriteRepository.save(updatedUser);

        userReadRepository.save(UserDTO.fromUser(updatedUser));
    }
}
