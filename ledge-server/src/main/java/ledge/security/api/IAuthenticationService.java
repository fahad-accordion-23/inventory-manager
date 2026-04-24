package ledge.security.api;

import ledge.security.api.exceptions.AuthenticationException;

public interface IAuthenticationService {
    String login(String username, String password) throws AuthenticationException;

    void logout(String token);
}
