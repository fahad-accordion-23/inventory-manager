package ledge.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ledge.infrastructure.ProductRepository;

public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Cannot add a null product");
        }
        return repository.save(product);
    }

    public Optional<Product> getProductById(UUID id) {
        return repository.findById(id);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public boolean deleteProduct(UUID id) {
        return repository.deleteById(id);
    }
}