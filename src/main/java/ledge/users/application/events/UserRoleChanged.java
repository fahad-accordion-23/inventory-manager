package ledge.users.application.events;

import java.util.UUID;

public class UserRoleChanged implements UsersUpdatedEvent {
    private final UUID userId;

    public UserRoleChanged(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
