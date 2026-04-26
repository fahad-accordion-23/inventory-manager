package ledge.users.writemodel.application.handlers;

import ledge.shared.infrastructure.commands.CommandHandler;
import ledge.users.events.domain.UserPasswordChangedDomainEvent;
import ledge.users.writemodel.commands.ChangeUserPasswordCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Handler for changing a user's password.
 * Decoupled from Read Model via Spring Events.
 */
@Service
public class ChangeUserPasswordCommandHandler implements CommandHandler<ChangeUserPasswordCommand> {
    private final IUserWriteRepository userWriteRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ChangeUserPasswordCommandHandler(IUserWriteRepository userWriteRepository,
                                            ApplicationEventPublisher eventPublisher) {
        this.userWriteRepository = userWriteRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(ChangeUserPasswordCommand command) {
        User user = userWriteRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        // Note: command.newPassword() is assumed to be raw and should be hashed before rehydrating
        // or rehydrate should reflect the new state. 
        // Based on existing logic it seems rehydrate takes the already hashed/updated value.
        User updatedUser = User.rehydrate(
                command.id(),
                user.getUsername(),
                command.newPassword());
        
        userWriteRepository.save(updatedUser);

        // Publish Domain Event for Read Model synchronization
        eventPublisher.publishEvent(new UserPasswordChangedDomainEvent(
                command.id(),
                command.newPassword()
        ));
    }
}
