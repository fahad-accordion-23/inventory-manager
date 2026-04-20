package ledge.inventory.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ledge.inventory.domain.Product;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepository implements IProductRepository {
    private static final String DATA_DIR = System.getProperty("user.dir") + "/data";
    private static final String FILE_PATH = DATA_DIR + "/inventory.json";
    private final List<Product> database = new ArrayList<>();
    private final Gson gson;

    public ProductRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        new File(DATA_DIR).mkdirs();
        loadDatabase();
    }

    private void loadDatabase() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type listType = new TypeToken<ArrayList<Product>>() {
                }.getType();
                List<Product> loadedProducts = gson.fromJson(reader, listType);
                if (loadedProducts != null) {
                    database.addAll(loadedProducts);
                }
            } catch (IOException e) {
                System.err.println("Failed to load inventory database: " + e.getMessage());
            }
        }
    }

    private void saveDatabase() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(database, writer);
        } catch (IOException e) {
            System.err.println("Failed to save inventory database: " + e.getMessage());
        }
    }

    @Override
    public Product save(Product product) {
        database.removeIf(p -> p.getId().equals(product.getId()));
        database.add(product);
        saveDatabase();
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
        boolean removed = database.removeIf(p -> p.getId().equals(id));
        if (removed) {
            saveDatabase();
        }
        return removed;
    }
}