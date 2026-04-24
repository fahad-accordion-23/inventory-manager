package ledge.users.writemodel.application.handlers;

import org.springframework.stereotype.Service;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import ledge.users.writemodel.commands.RemoveUserCommand;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;

@Service
public class RemoveUserCommandHandler implements CommandHandler<RemoveUserCommand> {
    private final IUserWriteRepository userWriteRepository;
    private final IUserReadRepository userReadRepository;

    public RemoveUserCommandHandler(IUserWriteRepository userWriteRepository, IUserReadRepository userReadRepository) {
        this.userWriteRepository = userWriteRepository;
        this.userReadRepository = userReadRepository;
    }

    @Override
    public void handle(RemoveUserCommand command) {
        userWriteRepository.delete(command.id());
        userReadRepository.deleteById(command.id());
    }
}
