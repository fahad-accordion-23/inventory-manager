package ledge.inventory.events.domain;

import java.util.UUID;

/**
 * Domain event fired when a product is removed from the inventory.
 */
public record ProductRemovedDomainEvent(
    UUID id
) {}
