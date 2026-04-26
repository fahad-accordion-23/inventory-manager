package ledge.users.events.domain;

import java.util.UUID;

/**
 * Domain event fired when a new user is created.
 * Primary purpose: Synchronize the Read Model.
 */
public record UserCreatedDomainEvent(
    UUID id,
    String username,
    String passwordHash
) {}
