package ledge.users.readmodel.application;

import ledge.users.events.domain.UserCreatedDomainEvent;
import ledge.users.events.domain.UserDeletedDomainEvent;
import ledge.users.events.domain.UserPasswordChangedDomainEvent;
import ledge.users.events.domain.UsernameChangedDomainEvent;
import ledge.users.readmodel.dtos.UserDTO;
import ledge.users.readmodel.infrastructure.IUserReadRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener responsible for keeping the User Read Model in sync with Domain
 * changes.
 */
@Component
public class UserReadModelSyncListener {
    private final IUserReadRepository userReadRepository;

    public UserReadModelSyncListener(IUserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @EventListener
    public void onUserCreated(UserCreatedDomainEvent event) {
        userReadRepository.save(new UserDTO(
                event.id(),
                event.username(),
                event.passwordHash()));
    }

    @EventListener
    public void onUsernameChanged(UsernameChangedDomainEvent event) {
        userReadRepository.findById(event.id()).ifPresent(user -> {
            userReadRepository.save(new UserDTO(
                    user.id(),
                    event.newUsername(),
                    user.hashedPassword()));
        });
    }

    @EventListener
    public void onUserPasswordChanged(UserPasswordChangedDomainEvent event) {
        userReadRepository.findById(event.id()).ifPresent(user -> {
            userReadRepository.save(new UserDTO(
                    user.id(),
                    user.username(),
                    event.newPasswordHash()));
        });
    }

    @EventListener
    public void onUserDeleted(UserDeletedDomainEvent event) {
        userReadRepository.deleteById(event.id());
    }
}
