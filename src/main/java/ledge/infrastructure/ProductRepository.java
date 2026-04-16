package ledge.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ledge.inventory.domain.Product;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    List<Product> findAll();
    boolean deleteById(UUID id);
}