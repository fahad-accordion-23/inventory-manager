package ledge.inventory.application.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ledge.inventory.domain.Product;
import ledge.inventory.infrastructure.IProductRepository;

public class ProductService {
    private final IProductRepository repository;

    public ProductService(IProductRepository repository) {
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

    public Product updateProduct(Product product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Cannot update a null product or product without ID");
        }
        return repository.save(product);
    }

    public boolean deleteProduct(UUID id) {
        return repository.deleteById(id);
    }
}