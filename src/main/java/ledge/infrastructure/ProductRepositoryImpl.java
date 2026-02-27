package ledge.infrastructure;

import ledge.domain.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepositoryImpl implements ProductRepository {
    private final List<Product> database = new ArrayList<>();

    @Override
    public Product save(Product product) {
        database.removeIf(p -> p.getId().equals(product.getId()));
        database.add(product);
        return product;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return database.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst();
    }

    @Override
    public List<Product> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(database));
    }

    @Override
    public boolean deleteById(UUID id) {
        return database.removeIf(p -> p.getId().equals(id));
    }
}