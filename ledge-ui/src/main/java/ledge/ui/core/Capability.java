package ledge.ui.core;

import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;

public enum Capability {

    VIEW_DASHBOARD(new Permission(Resource.PRODUCT, Action.READ)), // adjust if dashboard becomes its own resource

    VIEW_PRODUCTS(new Permission(Resource.PRODUCT, Action.READ)),
    CREATE_PRODUCT(new Permission(Resource.PRODUCT, Action.CREATE)),
    EDIT_PRODUCT(new Permission(Resource.PRODUCT, Action.UPDATE)),
    DELETE_PRODUCT(new Permission(Resource.PRODUCT, Action.DELETE)),

    VIEW_USERS(new Permission(Resource.USER, Action.READ)),
    CREATE_USER(new Permission(Resource.USER, Action.CREATE)),
    EDIT_USER(new Permission(Resource.USER, Action.UPDATE)),
    DELETE_USER(new Permission(Resource.USER, Action.DELETE)),

    VIEW_INVOICES(new Permission(Resource.INVOICE, Action.READ)),
    CREATE_INVOICE(new Permission(Resource.INVOICE, Action.CREATE));

    private final Permission permission;

    Capability(Permission permission) {
        this.permission = permission;
    }

    public Permission permission() {
        return permission;
    }
}