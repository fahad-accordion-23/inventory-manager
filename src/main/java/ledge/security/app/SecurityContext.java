package ledge.security.app;

import ledge.security.domain.User;

public final class SecurityContext {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    private SecurityContext() {
        // Utility class
    }

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static User getCurrentUser() {
        User user = currentUser.get();
        if (user == null) {
            throw new IllegalStateException("No user is currently authenticated");
        }
        return user;
    }

    public static void clear() {
        currentUser.remove();
    }

    public static boolean isAuthenticated() {
        return currentUser.get() != null;
    }
}
