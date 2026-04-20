package ledge.inventory.application.query;

import ledge.inventory.application.dtos.ProductDTO;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;
import ledge.util.cqrs.Query;
import java.util.List;
import java.util.Optional;

public record GetAllProductsQuery() implements Query<List<ProductDTO>> {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.READ);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
