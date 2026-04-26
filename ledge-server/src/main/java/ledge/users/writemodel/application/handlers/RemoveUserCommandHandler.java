package ledge.users.writemodel.application.handlers;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.events.domain.UserDeletedDomainEvent;
import ledge.users.events.integration.UserDeletedIntegrationEvent;
import ledge.users.writemodel.commands.RemoveUserCommand;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Handler for removing a user.
 * Decoupled from other contexts and Read Model via Spring Events.
 */
@Service
public class RemoveUserCommandHandler implements CommandHandler<RemoveUserCommand> {
    private final IUserWriteRepository userWriteRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RemoveUserCommandHandler(IUserWriteRepository userWriteRepository, 
                                    ApplicationEventPublisher eventPublisher) {
        this.userWriteRepository = userWriteRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(RemoveUserCommand command) {
        userWriteRepository.delete(command.id());

        // Publish Domain Event for local synchronization (Read Model)
        eventPublisher.publishEvent(new UserDeletedDomainEvent(command.id()));

        // Publish Integration Event for cross-context cleanup (Security)
        eventPublisher.publishEvent(new UserDeletedIntegrationEvent(command.id()));
    }
}
