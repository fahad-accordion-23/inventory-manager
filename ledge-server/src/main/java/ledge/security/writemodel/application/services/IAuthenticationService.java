package ledge.security.writemodel.application.services;

import ledge.security.writemodel.application.events.AuthenticationException;

public interface IAuthenticationService {
    String login(String username, String password) throws AuthenticationException;

    void logout(String token);
}
