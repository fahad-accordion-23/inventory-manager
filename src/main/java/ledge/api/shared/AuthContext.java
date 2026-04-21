package ledge.api.shared;

/**
 * Encapsulates authentication data (e.g., session token) for API requests.
 */
public record AuthContext(String token) {}
