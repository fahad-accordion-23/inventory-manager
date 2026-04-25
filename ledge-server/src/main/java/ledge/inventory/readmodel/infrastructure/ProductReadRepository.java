package ledge.inventory.readmodel.infrastructure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ledge.inventory.readmodel.dtos.ProductDTO;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

/**
 * Implementation of ProductReadRepository optimized for high-read performance.
 * Uses a ConcurrentHashMap for O(1) lookups and thread-safe operations.
 * Initializes data from a JSON file.
 */
@Repository
public class ProductReadRepository implements IProductReadRepository {

    private final Map<UUID, ProductDTO> products = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public ProductReadRepository() {
        loadInitialData();
    }

    private void loadInitialData() {
        java.io.File file = new java.io.File("data/inventory.json");
        if (!file.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<ProductDTO>>() {}.getType();
            List<ProductDTO> initialProducts = gson.fromJson(reader, listType);
            if (initialProducts != null) {
                for (ProductDTO product : initialProducts) {
                    save(product);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: Failed to load initial inventory from data/inventory.json: " + e.getMessage());
        }
    }

    @Override
    public void save(ProductDTO product) {
        if (product != null && product.id() != null) {
            products.put(product.id(), product);
        }
    }

    @Override
    public void delete(UUID id) {
        if (id != null) {
            products.remove(id);
        }
    }

    @Override
    public ProductDTO findById(UUID id) {
        if (id == null) return null;
        return products.get(id);
    }

    @Override
    public List<ProductDTO> findAll() {
        return new ArrayList<>(products.values());
    }

}
