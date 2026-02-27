package ledge;

import ledge.domain.Product;
import ledge.domain.ProductService;
import ledge.infrastructure.ProductRepository;
import ledge.infrastructure.ProductRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;

public class App {
    public static void main(String[] args) {
        ProductRepository repository = new ProductRepositoryImpl();
        ProductService productService = new ProductService(repository);

        System.out.println("Initializing Ledge Inventory Manager...\n");

        Product laptop = new Product(
            "Developer Laptop",
            new BigDecimal("1200.00"),
            new BigDecimal("1500.00"),
            10,
            new BigDecimal("0.15") // 15% tax
        );

        Product mouse = new Product(
            "Wireless Mouse",
            new BigDecimal("25.00"),
            new BigDecimal("45.00"),
            50,
            null // Demonstrates the 0% default tax rate requirement
        );

        productService.addProduct(laptop);
        productService.addProduct(mouse);

        System.out.println("Current Inventory:");
        List<Product> inventory = productService.getAllProducts();
        inventory.forEach(System.out::println);

        System.out.println("\nRetrieving specific product by ID...");
        productService.getProductById(laptop.getId())
            .ifPresent(p -> System.out.println("Found: " + p.getName()));
    }
}