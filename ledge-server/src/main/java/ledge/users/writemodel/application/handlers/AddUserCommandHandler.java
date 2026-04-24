package ledge.users.writemodel.application.handlers;

import org.springframework.stereotype.Service;

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

    public AddUserCommandHandler(IUserWriteRepository userWriteRepository, IUserReadRepository userReadRepository) {
        this.userWriteRepository = userWriteRepository;
        this.userReadRepository = userReadRepository;
    }

    @Override
    public void handle(AddUserCommand command) {
        String passwordHash = PasswordHasher.hash(command.password());
        User user = User.register(command.username(), passwordHash);
        userWriteRepository.save(user);

        userReadRepository.save(UserDTO.fromUser(user));
    }
}
