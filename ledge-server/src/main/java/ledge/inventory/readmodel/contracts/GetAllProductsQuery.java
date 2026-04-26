package ledge.inventory.readmodel.contracts;

import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.security.api.annotations.RequiresPermission;
import ledge.shared.security.models.Action;
import ledge.shared.security.models.Resource;
import ledge.shared.infrastructure.queries.Query;

import java.util.List;

@RequiresPermission(resource = Resource.PRODUCT, action = Action.READ)
public record GetAllProductsQuery() implements Query<List<ProductDTO>> {
}
