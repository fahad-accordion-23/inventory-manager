package ledge.users.events.integration;

import java.util.UUID;

/**
 * Integration event exported to other contexts.
 * Fired when a user successfully registers in the system.
 */
public record UserRegisteredIntegrationEvent(
    UUID id,
    String username
) {}
