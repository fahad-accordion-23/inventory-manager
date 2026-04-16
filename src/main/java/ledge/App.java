package ledge;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ledge.inventory.app.InventoryCommandBus;
import ledge.inventory.app.InventoryEventBroker;
import ledge.inventory.app.InventoryQueryBus;
import ledge.inventory.app.ProductController;
import ledge.inventory.domain.ProductService;
import ledge.infrastructure.ProductRepositoryImpl;
import ledge.infrastructure.UserJsonRepository;
import ledge.security.app.AuthController;
import ledge.security.app.AuthService;
import ledge.security.app.event.LoginSucceededEvent;
import ledge.security.app.event.UserLoggedOutEvent;
import ledge.ui.LoginView;
import ledge.ui.MainLayout;
import ledge.ui.Sidebar;
import ledge.util.event.Subscribe;

public class App extends Application {

    private InventoryEventBroker eventBroker;
    private InventoryCommandBus commandBus;
    private InventoryQueryBus queryBus;

    private ProductController productController;
    private AuthController authController;

    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        eventBroker = new InventoryEventBroker();
        commandBus = new InventoryCommandBus();
        queryBus = new InventoryQueryBus();

        // Product Setup
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        ProductService productService = new ProductService(productRepository);
        productController = new ProductController(productService, eventBroker);

        // Security Setup
        UserJsonRepository userRepository = new UserJsonRepository();
        AuthService authService = new AuthService(userRepository);
        authController = new AuthController(authService, eventBroker);

        eventBroker.register(this);
        commandBus.register(authController);
        commandBus.register(productController);
        queryBus.register(productController);
    }

    @Subscribe
    private void onLoginSucceeded(LoginSucceededEvent event) {
        Platform.runLater(this::showMainScene);
    }

    @Subscribe
    private void onUserLoggedOut(UserLoggedOutEvent event) {
        Platform.runLater(this::showLoginScene);
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
                    return new LoginView(eventBroker, commandBus);
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
                    return new MainLayout(eventBroker, commandBus, queryBus);
                }
                if (param == Sidebar.class) {
                    return new Sidebar(eventBroker, commandBus);
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