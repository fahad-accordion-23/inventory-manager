package ledge.ui.events;

import ledge.util.event.Event;

public class ShowNotificationEvent implements Event {
    private final String message;
    private final boolean isError;

    public ShowNotificationEvent(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return isError;
    }
}
