package ledge.inventory.readmodel.contracts;

import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.security.internal.domain.models.Action;
import ledge.security.internal.domain.models.Permission;
import ledge.security.internal.domain.models.Resource;
import ledge.shared.infrastructure.queries.Query;

import java.util.List;
import java.util.Optional;

public record GetAllProductsQuery() implements Query<List<ProductDTO>> {
    public static final Permission REQUIRED = new Permission(Resource.PRODUCT, Action.READ);

    @Override
    public Optional<Permission> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
