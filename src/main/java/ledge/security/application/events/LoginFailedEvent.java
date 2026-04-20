package ledge.security.application.events;

import ledge.util.event.Event;

public class LoginFailedEvent implements Event {
    private final String reason;

    public LoginFailedEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
