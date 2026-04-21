package ledge.inventory.writemodel.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ledge.inventory.writemodel.domain.Product;

public interface IProductWriteRepository {
    Product save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAll();

    void delete(UUID id);
}