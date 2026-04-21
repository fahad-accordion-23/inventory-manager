package ledge.ui.events;

import ledge.shared.infrastructure.events.Event;

public class LoginFailedEvent implements Event {
    private final String reason;

    public LoginFailedEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
