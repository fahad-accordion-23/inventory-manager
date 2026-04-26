package ledge.users.writemodel.application.handlers;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.events.domain.UsernameChangedDomainEvent;
import ledge.users.writemodel.commands.ChangeUsernameCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Handler for changing a user's username.
 * Decoupled from Read Model via Spring Events.
 */
@Service
public class ChangeUsernameCommandHandler implements CommandHandler<ChangeUsernameCommand> {
    private final IUserWriteRepository userWriteRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ChangeUsernameCommandHandler(IUserWriteRepository userWriteRepository,
                                        ApplicationEventPublisher eventPublisher) {
        this.userWriteRepository = userWriteRepository;
        this.eventPublisher = eventPublisher;
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

        // Publish Domain Event for Read Model synchronization
        eventPublisher.publishEvent(new UsernameChangedDomainEvent(
                command.id(),
                command.newUsername()
        ));
    }
}
