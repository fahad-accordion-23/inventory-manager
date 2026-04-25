package ledge.ui.core;

import ledge.security.api.dto.PermissionDTO;
import ledge.security.api.models.Action;
import ledge.security.api.models.Resource;

/**
 * High-level UI capabilities mapped to underlying PermissionDTO contracts.
 */
public enum Capability {

    VIEW_DASHBOARD(new PermissionDTO(Resource.PRODUCT, Action.READ)),

    VIEW_PRODUCTS(new PermissionDTO(Resource.PRODUCT, Action.READ)),
    CREATE_PRODUCT(new PermissionDTO(Resource.PRODUCT, Action.CREATE)),
    EDIT_PRODUCT(new PermissionDTO(Resource.PRODUCT, Action.UPDATE)),
    DELETE_PRODUCT(new PermissionDTO(Resource.PRODUCT, Action.DELETE)),

    VIEW_USERS(new PermissionDTO(Resource.USER, Action.READ)),
    CREATE_USER(new PermissionDTO(Resource.USER, Action.CREATE)),
    EDIT_USER(new PermissionDTO(Resource.USER, Action.UPDATE)),
    DELETE_USER(new PermissionDTO(Resource.USER, Action.DELETE)),

    VIEW_INVOICES(new PermissionDTO(Resource.INVOICE, Action.READ)),
    CREATE_INVOICE(new PermissionDTO(Resource.INVOICE, Action.CREATE)),

    VIEW_ROLES(new PermissionDTO(Resource.ROLE, Action.READ)),
    MANAGE_ROLES(new PermissionDTO(Resource.ROLE, Action.UPDATE));

    private final PermissionDTO permission;

    Capability(PermissionDTO permission) {
        this.permission = permission;
    }

    public PermissionDTO permission() {
        return permission;
    }
}