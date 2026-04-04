package ledge;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ledge.application.InventoryEventBroker;
import ledge.application.ProductController;
import ledge.domain.ProductService;
import ledge.infrastructure.ProductRepositoryImpl;
import ledge.ui.MainLayout;

public class App extends Application {

    private InventoryEventBroker eventBroker;
    private ProductController productController;

    @Override
    public void init() throws Exception {
        ProductRepositoryImpl repository = new ProductRepositoryImpl();
        ProductService productService = new ProductService(repository);
        eventBroker = new InventoryEventBroker();
        productController = new ProductController(productService, eventBroker);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/MainLayout.fxml"));
        loader.setControllerFactory(param -> {
            if (param == MainLayout.class) {
                return new MainLayout(eventBroker);
            }
            try {
                return param.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Parent root = loader.load();

        primaryStage.setTitle("Ledge Inventory Manager");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}