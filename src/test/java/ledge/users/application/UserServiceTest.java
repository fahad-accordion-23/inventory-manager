package ledge.users.application;

import ledge.shared.types.Role;
import ledge.users.application.services.UserService;
import ledge.users.domain.User;
import ledge.users.infrastructure.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private MockUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new MockUserRepository();
        userService = new UserService(userRepository);
    }

    @Test
    void testAddUser() {
        User user = new User(UUID.randomUUID(), "testUser", "hash", Role.ADMIN);
        userService.addUser(user);

        assertEquals(1, userRepository.database.size());
        assertEquals("testUser", userRepository.database.get(0).getUsername());
    }

    @Test
    void testUpdateUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "testUser", "hash", Role.ADMIN);
        userService.addUser(user);

        User updatedUser = new User(id, "testUserUpdated", "newHash", Role.INVENTORY_MANAGER);
        userService.updateUser(updatedUser);

        assertEquals(1, userRepository.database.size());
        assertEquals("testUserUpdated", userRepository.database.get(0).getUsername());
        assertEquals(Role.INVENTORY_MANAGER, userRepository.database.get(0).getRole());
    }

    @Test
    void testRemoveUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "testUser", "hash", Role.ADMIN);
        userService.addUser(user);

        assertEquals(1, userRepository.database.size());

        userService.removeUser(id);

        assertEquals(0, userRepository.database.size());
    }

    @Test
    void testGetAllUsers() {
        userService.addUser(new User(UUID.randomUUID(), "user1", "h1", Role.ADMIN));
        userService.addUser(new User(UUID.randomUUID(), "user2", "h2", Role.SALES_STAFF));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    // A simple in-memory repository for testing
    static class MockUserRepository implements IUserRepository {
        List<User> database = new ArrayList<>();

        @Override
        public Optional<User> findByUsername(String username) {
            return database.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(database);
        }

        @Override
        public void save(User user) {
            database.removeIf(u -> u.getId().equals(user.getId()));
            database.add(user);
        }

        @Override
        public void delete(UUID id) {
            database.removeIf(u -> u.getId().equals(id));
        }
    }
}
