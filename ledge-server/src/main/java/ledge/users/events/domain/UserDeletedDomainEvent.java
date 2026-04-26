package ledge.users.events.domain;

import java.util.UUID;

/**
 * Domain event fired when a user is removed.
 * Primary purpose: Synchronize the Read Model.
 */
public record UserDeletedDomainEvent(
    UUID id
) {}
