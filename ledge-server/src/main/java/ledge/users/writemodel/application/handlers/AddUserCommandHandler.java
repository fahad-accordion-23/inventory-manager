package ledge.users.writemodel.application.handlers;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.events.domain.UserCreatedDomainEvent;
import ledge.users.events.integration.UserRegisteredIntegrationEvent;
import ledge.users.writemodel.commands.AddUserCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import ledge.util.PasswordHasher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Handler for adding a new user.
 * Decoupled from Security context and Read Model via Spring Events.
 */
@Service
public class AddUserCommandHandler implements CommandHandler<AddUserCommand> {
    private final IUserWriteRepository userWriteRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AddUserCommandHandler(IUserWriteRepository userWriteRepository,
                                 ApplicationEventPublisher eventPublisher) {
        this.userWriteRepository = userWriteRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(AddUserCommand command) {
        String passwordHash = PasswordHasher.hash(command.password());
        User user = User.register(command.username(), passwordHash);
        
        userWriteRepository.save(user);

        // Publish Domain Event for local synchronization (e.g. Read Model)
        eventPublisher.publishEvent(new UserCreatedDomainEvent(
                user.getId(), 
                user.getUsername(), 
                user.getPasswordHash()
        ));

        // Publish Integration Event for cross-context side-effects (e.g. Role Assignment)
        eventPublisher.publishEvent(new UserRegisteredIntegrationEvent(
                user.getId(), 
                user.getUsername()
        ));
    }
}
