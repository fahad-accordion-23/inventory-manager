package ledge.users.application.events;

import java.util.UUID;

public class UserRemovedEvent implements UsersUpdatedEvent {
    private final UUID userId;

    public UserRemovedEvent(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
