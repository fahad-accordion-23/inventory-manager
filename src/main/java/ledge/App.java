package ledge;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ledge.application.InventoryEventBroker;
import ledge.application.ProductController;
import ledge.domain.ProductService;
import ledge.infrastructure.ProductRepositoryImpl;
import ledge.infrastructure.UserJsonRepository;
import ledge.security.AuthController;
import ledge.security.AuthService;
import ledge.security.event.LoginSucceededEvent;
import ledge.security.event.LogoutRequestedEvent;
import ledge.ui.LoginView;
import ledge.ui.MainLayout;

public class App extends Application {

    private InventoryEventBroker eventBroker;
    
    // Kept to prevent garbage collection of handlers
    @SuppressWarnings("unused")
    private ProductController productController;
    
    @SuppressWarnings("unused")
    private AuthController authController;
    
    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        eventBroker = new InventoryEventBroker();

        // Product Setup
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        ProductService productService = new ProductService(productRepository);
        productController = new ProductController(productService, eventBroker);

        // Security Setup
        UserJsonRepository userRepository = new UserJsonRepository();
        AuthService authService = new AuthService(userRepository);
        authController = new AuthController(authService, eventBroker);
        
        // Subscribe to auth events for scene switching
        eventBroker.subscribe(LoginSucceededEvent.class, _ -> Platform.runLater(this::showMainScene));
        eventBroker.subscribe(LogoutRequestedEvent.class, _ -> Platform.runLater(this::showLoginScene));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Ledge Inventory Manager");
        showLoginScene();
        primaryStage.show();
    }

    private void showLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/LoginView.fxml"));
            loader.setControllerFactory(param -> {
                if (param == LoginView.class) {
                    return new LoginView(eventBroker);
                }
                try {
                    return param.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMainScene() {
        try {
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
            primaryStage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}