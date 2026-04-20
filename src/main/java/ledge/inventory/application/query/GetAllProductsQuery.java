package ledge.inventory.application.query;

import ledge.inventory.application.dtos.ProductDTO;
import ledge.security.domain.Action;
import ledge.security.domain.Permission;
import ledge.security.domain.Resource;
import ledge.util.cqrs.Query;
import java.util.List;
import java.util.Optional;

public class GetAllProductsQuery implements Query<List<ProductDTO>> {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.READ);

    public GetAllProductsQuery() {
    }

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
