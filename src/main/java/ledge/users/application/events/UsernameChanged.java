package ledge.users.application.events;

import java.util.UUID;

public class UsernameChanged implements UsersUpdatedEvent {
    private final UUID userId;

    public UsernameChanged(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
