package ledge.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void testHash() {
        String password = "mySecretPassword";
        String hash = PasswordHasher.hash(password);
        
        assertNotNull(hash);
        assertNotEquals(password, hash);
    }

    @Test
    void testVerify_Success() {
        String password = "mySecretPassword";
        String hash = PasswordHasher.hash(password);
        
        assertTrue(PasswordHasher.verify(password, hash));
    }

    @Test
    void testVerify_Failure() {
        String password = "mySecretPassword";
        String hash = PasswordHasher.hash(password);
        
        assertFalse(PasswordHasher.verify("wrongPassword", hash));
    }
}
