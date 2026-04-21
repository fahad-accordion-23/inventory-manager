package ledge.inventory.readmodel.contracts;

import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.shared.infrastructure.queries.Query;
import ledge.shared.types.Action;
import ledge.shared.types.Permission;
import ledge.shared.types.Resource;

import java.util.List;
import java.util.Optional;

public record GetAllProductsQuery() implements Query<List<ProductDTO>> {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.READ);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
