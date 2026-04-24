package ledge.users.writemodel.domain;

import ledge.shared.types.Role;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testRegisterUser() {
        User user = User.register("admin", "hashed_pass", Role.ADMIN);
        
        assertNotNull(user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("hashed_pass", user.getPasswordHash());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void testRehydrateUser() {
        UUID id = UUID.randomUUID();
        User user = User.rehydrate(id, "manager", "pass", Role.INVENTORY_MANAGER);
        
        assertEquals(id, user.getId());
        assertEquals("manager", user.getUsername());
        assertEquals(Role.INVENTORY_MANAGER, user.getRole());
    }

    @Test
    void testRegistrationConstraints() {
        assertThrows(NullPointerException.class, () -> User.register(null, "hash", Role.ADMIN));
        assertThrows(NullPointerException.class, () -> User.register("user", null, Role.ADMIN));
        assertThrows(NullPointerException.class, () -> User.register("user", "hash", null));
    }
}
