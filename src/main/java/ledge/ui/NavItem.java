package ledge.ui;

import ledge.domain.Permission;

/**
 * Describes a single navigation entry in the sidebar.
 * Owns the required permission — so the Sidebar never needs to hardcode (Resource, Action) pairs.
 */
public record NavItem(String label, Permission required, Runnable action) {
}
