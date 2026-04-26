package ledge.inventory.readmodel.contracts;

import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.security.api.dto.PermissionDTO;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.shared.infrastructure.queries.Query;

import java.util.List;
import java.util.Optional;

public record GetAllProductsQuery() implements Query<List<ProductDTO>> {
    public static final PermissionDTO REQUIRED = new PermissionDTO(Resource.PRODUCT, Action.READ);

    @Override
    public Optional<PermissionDTO> getRequiredPermission() {
        return Optional.of(REQUIRED);
    }
}
