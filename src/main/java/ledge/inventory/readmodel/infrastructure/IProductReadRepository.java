package ledge.inventory.readmodel.infrastructure;

import java.util.List;
import java.util.UUID;

import ledge.inventory.readmodel.dtos.ProductDTO;

public interface IProductReadRepository {
    void save(ProductDTO product);

    void delete(UUID id);

    ProductDTO findById(UUID id);

    List<ProductDTO> findAll();
}
