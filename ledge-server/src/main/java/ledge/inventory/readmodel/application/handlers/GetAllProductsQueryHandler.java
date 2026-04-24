package ledge.inventory.readmodel.application.handlers;

import ledge.inventory.readmodel.contracts.GetAllProductsQuery;
import ledge.inventory.readmodel.dtos.ProductDTO;
import ledge.inventory.readmodel.infrastructure.IProductReadRepository;
import ledge.shared.infrastructure.queries.QueryHandler;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GetAllProductsQueryHandler implements QueryHandler<GetAllProductsQuery, List<ProductDTO>> {
    private final IProductReadRepository readProductRepository;

    public GetAllProductsQueryHandler(IProductReadRepository readProductRepository) {
        this.readProductRepository = readProductRepository;
    }

    @Override
    public List<ProductDTO> handle(GetAllProductsQuery query) {
        return readProductRepository.findAll();
    }
}
