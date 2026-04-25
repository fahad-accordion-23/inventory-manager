package ledge.ui.core;

import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;

/**
 * High-level UI capabilities mapped to underlying security Resource and Action.
 * PermissionResponseDTO was removed intentionally; capabilities now store raw
 * Resource/Action pairs.
 */
public enum Capability {

    VIEW_DASHBOARD(Resource.PRODUCT, Action.READ),

    VIEW_PRODUCTS(Resource.PRODUCT, Action.READ),
    CREATE_PRODUCT(Resource.PRODUCT, Action.CREATE),
    EDIT_PRODUCT(Resource.PRODUCT, Action.UPDATE),
    DELETE_PRODUCT(Resource.PRODUCT, Action.DELETE),

    VIEW_USERS(Resource.USER, Action.READ),
    CREATE_USER(Resource.USER, Action.CREATE),
    EDIT_USER(Resource.USER, Action.UPDATE),
    DELETE_USER(Resource.USER, Action.DELETE),

    VIEW_INVOICES(Resource.INVOICE, Action.READ),
    CREATE_INVOICE(Resource.INVOICE, Action.CREATE),

    VIEW_ROLES(Resource.ROLE, Action.READ),
    MANAGE_ROLES(Resource.ROLE, Action.UPDATE);

    private final Resource resource;
    private final Action action;

    Capability(Resource resource, Action action) {
        this.resource = resource;
        this.action = action;
    }

    public Resource resource() {
        return resource;
    }

    public Action action() {
        return action;
    }
}