package ledge.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductService {
    private final List<Product> productStore;

    public ProductService() {
        this.productStore = new ArrayList<>();
    }

    /**
     * Handles the domain logic for adding a product to the system.
     */
    public Product addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Cannot add a null product");
        }
        productStore.add(product);
        return product;
    }

    /**
     * Retrieves a product by its unique identifier.
     */
    public Optional<Product> getProductById(UUID id) {
        return productStore.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Returns an unmodifiable list of all current products.
     */
    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(productStore);
    }

    /**
     * Removes a product from the temporary storage.
     */
    public boolean deleteProduct(UUID id) {
        return productStore.removeIf(p -> p.getId().equals(id));
    }
}