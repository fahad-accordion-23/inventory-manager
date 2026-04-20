package ledge.security.application.services;

import ledge.security.application.events.AuthenticationException;
import ledge.security.domain.ISessionService;
import ledge.security.domain.SessionService;
import ledge.shared.types.Role;
import ledge.users.application.services.IUserService;
import ledge.users.domain.User;
import ledge.util.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    private IAuthenticationService authService;
    private ISessionService sessionService;
    private MockUserService userService;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService();
        userService = new MockUserService();
        authService = new AuthenticationService(userService, sessionService);
    }

    @Test
    void testSuccessfulLogin() throws AuthenticationException {
        // Setup user with hashed password
        String plainPassword = "mySecretPassword";
        String hash = PasswordHasher.hash(plainPassword);
        User user = User.rehydrate(UUID.randomUUID(), "testUser", hash, Role.ADMIN);
        userService.users.add(user);

        // Perform login
        String token = authService.login("testUser", plainPassword);

        assertNotNull(token);
        assertTrue(sessionService.getUserByToken(token).isPresent());
        assertEquals("testUser", sessionService.getUserByToken(token).get().username());
    }

    @Test
    void testFailedLogin_WrongUsername() {
        String hash = PasswordHasher.hash("password");
        User user = User.rehydrate(UUID.randomUUID(), "testUser", hash, Role.ADMIN);
        userService.users.add(user);

        assertThrows(AuthenticationException.class, () -> {
            authService.login("wrongUser", "password");
        });
    }

    @Test
    void testFailedLogin_WrongPassword() {
        String hash = PasswordHasher.hash("password");
        User user = User.rehydrate(UUID.randomUUID(), "testUser", hash, Role.ADMIN);
        userService.users.add(user);

        assertThrows(AuthenticationException.class, () -> {
            authService.login("testUser", "wrongPassword");
        });
    }

    @Test
    void testLogout() throws AuthenticationException {
        String hash = PasswordHasher.hash("pass");
        User user = User.rehydrate(UUID.randomUUID(), "user", hash, Role.ADMIN);
        userService.users.add(user);

        String token = authService.login("user", "pass");
        assertTrue(sessionService.getUserByToken(token).isPresent());

        authService.logout(token);
        assertFalse(sessionService.getUserByToken(token).isPresent());
    }

    static class MockUserService implements IUserService {
        List<User> users = new ArrayList<>();

        @Override
        public void addUser(String username, String password, Role role) {
        }

        @Override
        public void updateUser(UUID id, String username, String password, Role role) {
        }

        @Override
        public void changeUserPassword(UUID id, String newPassword) {
        }

        @Override
        public void changeUserRole(UUID id, Role newRole) {
        }

        @Override
        public void changeUsername(UUID id, String newUsername) {
        }

        @Override
        public void removeUser(UUID userId) {
        }

        @Override
        public List<User> getAllUsers() {
            return users;
        }

        @Override
        public Optional<User> getUserById(UUID id) {
            return Optional.empty();
        }

        @Override
        public Optional<User> getUserByUsername(String username) {
            return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        }
    }
}
