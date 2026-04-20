package ledge.ui.views;

import ledge.shared.types.Permission;

/**
 * Describes a single navigation entry in the sidebar.
 * Owns the required permission — so the Sidebar never needs to hardcode
 * (Resource, Action) pairs.
 */
public record NavItem(String label, Permission requiredPermissions, Runnable action) {
}
