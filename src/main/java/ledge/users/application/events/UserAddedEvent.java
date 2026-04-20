package ledge.users.application.events;

import java.util.UUID;

public class UserAddedEvent implements UsersUpdatedEvent {
    private final UUID userId;

    public UserAddedEvent(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
