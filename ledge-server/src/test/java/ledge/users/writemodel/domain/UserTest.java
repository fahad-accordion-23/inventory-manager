package ledge.users.writemodel.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testRegisterUser() {
        User user = User.register("admin", "hashed_pass");
        
        assertNotNull(user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("hashed_pass", user.getPasswordHash());
    }

    @Test
    void testRehydrateUser() {
        UUID id = UUID.randomUUID();
        User user = User.rehydrate(id, "manager", "pass");
        
        assertEquals(id, user.getId());
        assertEquals("manager", user.getUsername());
    }

    @Test
    void testRegistrationConstraints() {
        // Since User uses Objects.requireNonNull
        assertThrows(NullPointerException.class, () -> User.register(null, "hash"));
        assertThrows(NullPointerException.class, () -> User.register("user", null));
    }
}
