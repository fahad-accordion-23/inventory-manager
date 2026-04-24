package ledge.inventory.readmodel.application.handlers;

import ledge.inventory.readmodel.contracts.GetAllProductsQuery;
import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.inventory.readmodel.infrastructure.IProductReadRepository;
import ledge.shared.infrastructure.queries.QueryHandler;

import java.util.List;

public class GetAllProductsQueryHandler {
    private final IProductReadRepository readProductRepository;

    public GetAllProductsQueryHandler(IProductReadRepository readProductRepository) {
        this.readProductRepository = readProductRepository;
    }

    @QueryHandler
    public List<ProductDTO> handleGetAllProducts(GetAllProductsQuery query) {
        return readProductRepository.findAll();
    }
}
