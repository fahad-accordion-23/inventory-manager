package ledge.security.event;

import ledge.util.event.Event;

public class LoginRequestedEvent implements Event {
    private final String username;
    private final String password;

    public LoginRequestedEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
