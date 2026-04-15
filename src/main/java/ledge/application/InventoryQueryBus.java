package ledge.application;

import ledge.security.AccessPolicy;
import ledge.util.cqrs.Query;
import ledge.util.cqrs.QueryBus;

public class InventoryQueryBus extends QueryBus {

    @Override
    public <R> R dispatch(Query<R> query) {
        query.getRequiredPermission().ifPresent(AccessPolicy::require);
        return super.dispatch(query);
    }
}
