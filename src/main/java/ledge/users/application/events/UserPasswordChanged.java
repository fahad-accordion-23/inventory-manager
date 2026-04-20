package ledge.users.application.events;

import java.util.UUID;

public class UserPasswordChanged implements UsersUpdatedEvent {
    private final UUID userId;

    public UserPasswordChanged(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
