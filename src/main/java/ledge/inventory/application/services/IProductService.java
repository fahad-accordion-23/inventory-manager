package ledge.inventory.application.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import ledge.inventory.domain.Product;

public interface IProductService {
    Product addProduct(Product product);

    Optional<Product> getProductById(UUID id);

    List<Product> getAllProducts();

    Product updateProduct(Product product);

    boolean deleteProduct(UUID id);
}
