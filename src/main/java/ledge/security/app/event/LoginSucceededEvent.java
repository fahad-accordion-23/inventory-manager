package ledge.security.app.event;

import ledge.security.domain.User;
import ledge.util.event.Event;

public class LoginSucceededEvent implements Event {
    private final User user;

    public LoginSucceededEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
