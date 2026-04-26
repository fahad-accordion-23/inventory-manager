package ledge.users.events.domain;

import java.util.UUID;

/**
 * Domain event fired when a user's username is changed.
 * Primary purpose: Synchronize the Read Model.
 */
public record UsernameChangedDomainEvent(
    UUID id,
    String newUsername
) {}
