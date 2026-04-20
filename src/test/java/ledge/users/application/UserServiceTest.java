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
        userService.addUser("testUser", "pass", Role.ADMIN);

        assertEquals(1, userRepository.database.size());
        assertEquals("testUser", userRepository.database.get(0).getUsername());
    }

    @Test
    void testUpdateUser() {
        userService.addUser("testUser", "pass", Role.ADMIN);
        UUID id = userRepository.database.get(0).getId();

        userService.updateUser(id, "testUserUpdated", "newPass", Role.INVENTORY_MANAGER);

        assertEquals(1, userRepository.database.size());
        assertEquals("testUserUpdated", userRepository.database.get(0).getUsername());
        assertEquals(Role.INVENTORY_MANAGER, userRepository.database.get(0).getRole());
    }

    @Test
    void testRemoveUser() {
        userService.addUser("testUser", "pass", Role.ADMIN);
        UUID id = userRepository.database.get(0).getId();

        assertEquals(1, userRepository.database.size());

        userService.removeUser(id);

        assertEquals(0, userRepository.database.size());
    }

    @Test
    void testGetAllUsers() {
        userService.addUser("user1", "pass1", Role.ADMIN);
        userService.addUser("user2", "pass2", Role.SALES_STAFF);

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    // A simple in-memory repository for testing
    static class MockUserRepository implements IUserRepository {
        List<User> database = new ArrayList<>();

        @Override
        public Optional<User> findById(UUID id) {
            return database.stream().filter(u -> u.getId().equals(id)).findFirst();
        }

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
            database.add(user);
        }

        @Override
        public void update(User user) {
            database.removeIf(u -> u.getId().equals(user.getId()));
            database.add(user);
        }

        @Override
        public void delete(UUID id) {
            database.removeIf(u -> u.getId().equals(id));
        }
    }
}
