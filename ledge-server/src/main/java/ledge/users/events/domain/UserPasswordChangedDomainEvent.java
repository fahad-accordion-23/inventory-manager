package ledge.users.events.domain;

import java.util.UUID;

/**
 * Domain event fired when a user's password is changed.
 * Primary purpose: Synchronize the Read Model.
 */
public record UserPasswordChangedDomainEvent(
    UUID id,
    String newPasswordHash
) {}
