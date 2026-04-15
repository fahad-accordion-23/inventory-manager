package ledge.application.query;

import ledge.application.dto.ProductDTO;
import ledge.domain.Action;
import ledge.domain.Permission;
import ledge.domain.Resource;
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
