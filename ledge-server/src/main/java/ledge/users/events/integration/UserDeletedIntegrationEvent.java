package ledge.users.events.integration;

import java.util.UUID;

/**
 * Integration event exported to other contexts.
 * Fired when a user account is deleted.
 */
public record UserDeletedIntegrationEvent(
    UUID id
) {}
