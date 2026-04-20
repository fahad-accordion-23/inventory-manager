package ledge.inventory.application.services;

import ledge.inventory.domain.Product;
import ledge.inventory.infrastructure.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;
    private MockProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MockProductRepository();
        productService = new ProductService(repository);
    }

    @Test
    void testAddProduct() {
        Product p = Product.register("Item 1", new BigDecimal("5.0"), new BigDecimal("10.0"), 50,
                new BigDecimal("0.1"));
        Product saved = productService.addProduct(p);

        assertNotNull(saved);
        assertEquals(1, repository.products.size());
        assertEquals("Item 1", repository.products.get(0).getName());
    }

    @Test
    void testUpdateProduct() {
        Product p = Product.register("Item 1", new BigDecimal("5.0"), new BigDecimal("10.0"), 50,
                new BigDecimal("0.1"));
        Product saved = productService.addProduct(p);

        saved.setName("Item 1 Updated");
        productService.updateProduct(saved);

        assertEquals("Item 1 Updated", repository.products.get(0).getName());
    }

    @Test
    void testGetProductById() {
        Product p = Product.register("Item 1", new BigDecimal("5.0"), new BigDecimal("10.0"), 50,
                new BigDecimal("0.1"));
        Product saved = productService.addProduct(p);

        Optional<Product> fetched = productService.getProductById(saved.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Item 1", fetched.get().getName());
    }

    @Test
    void testDeleteProduct() {
        Product p = Product.register("Item 1", new BigDecimal("5.0"), new BigDecimal("10.0"), 50,
                new BigDecimal("0.1"));
        Product saved = productService.addProduct(p);

        boolean deleted = productService.deleteProduct(saved.getId());
        assertTrue(deleted);
        assertEquals(0, repository.products.size());
    }

    @Test
    void testGetAllProducts() {
        productService.addProduct(
                Product.register("Item 1", new BigDecimal("5.0"), new BigDecimal("10.0"), 50, new BigDecimal("0.1")));
        productService.addProduct(
                Product.register("Item 2", new BigDecimal("10.0"), new BigDecimal("20.0"), 30, new BigDecimal("0.2")));

        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }

    static class MockProductRepository implements IProductRepository {
        List<Product> products = new ArrayList<>();

        @Override
        public Product save(Product product) {
            products.removeIf(p -> p.getId().equals(product.getId()));
            products.add(product);
            return product;
        }

        @Override
        public Optional<Product> findById(UUID id) {
            return products.stream().filter(p -> p.getId().equals(id)).findFirst();
        }

        @Override
        public List<Product> findAll() {
            return new ArrayList<>(products);
        }

        @Override
        public boolean deleteById(UUID id) {
            return products.removeIf(p -> p.getId().equals(id));
        }
    }
}
