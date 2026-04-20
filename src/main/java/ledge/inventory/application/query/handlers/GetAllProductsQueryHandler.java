package ledge.inventory.application.query.handlers;

import ledge.inventory.application.dtos.ProductDTO;
import ledge.inventory.application.query.GetAllProductsQuery;
import ledge.inventory.application.services.IProductService;
import ledge.util.cqrs.QueryHandler;

import java.util.List;

public class GetAllProductsQueryHandler {
    private final IProductService productService;

    public GetAllProductsQueryHandler(IProductService productService) {
        this.productService = productService;
    }

    @QueryHandler
    public List<ProductDTO> handleGetAllProducts(GetAllProductsQuery query) {
        return productService.getAllProducts()
                .stream()
                .map(ProductDTO::fromProduct)
                .toList();
    }
}
