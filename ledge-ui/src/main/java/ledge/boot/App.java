package ledge.boot;

import javafx.application.Application;
import javafx.stage.Stage;

import ledge.api.auth.AuthController;
import ledge.api.inventory.InventoryController;
import ledge.api.users.UserController;
import ledge.ui.core.SessionManager;
import ledge.ui.core.UIController;
import ledge.ui.messaging.UIEventBroker;

/**
 * Main application class.
 * Initializes the core components using the ModuleRegistry system
 * and hands off UI control to the UIController.
 */
public class App extends Application {

    private UIEventBroker uiEventBroker;
    private UIController uiController;
    private SessionManager sessionManager;
    private ModuleRegistry registry;

    @Override
    public void init() throws Exception {
        // Initialize the core architecture through the module system
        registry = Modules.bootstrap();

        // Initialize UI infrastructure
        uiEventBroker = new UIEventBroker();

        // SessionManager depends on AuthController from the API layer
        AuthController authController = registry.resolve(AuthController.class);
        sessionManager = new SessionManager(authController);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ledge Inventory Manager");

        // Resolve API controllers from the registry
        InventoryController inventoryController = registry.resolve(InventoryController.class);
        UserController userController = registry.resolve(UserController.class);

        // Initialize and start UI coordination
        uiController = new UIController(
                primaryStage,
                uiEventBroker,
                sessionManager,
                inventoryController,
                userController);

        uiController.showLoginScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}