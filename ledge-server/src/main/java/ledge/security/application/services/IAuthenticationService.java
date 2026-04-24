package ledge.security.application.services;

import ledge.security.application.events.AuthenticationException;

public interface IAuthenticationService {
    String login(String username, String password) throws AuthenticationException;

    void logout(String token);
}
